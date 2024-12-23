from flask import Flask, redirect, render_template,request, jsonify, session, url_for, abort, flash
from flask_login import (LoginManager,current_user,login_user, logout_user, login_required,AnonymousUserMixin)

from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import text

from datetime import datetime
from werkzeug.security import check_password_hash, generate_password_hash
#imports das passwords e redefinir password
from itsdangerous import URLSafeTimedSerializer as Serializer
from flask_mail import Mail, Message

#imports da BD
from models.softpm_database import User, Role, Company, userHasCompany, Employee, Ticket, Type, Status, db #importa a tabela user da BD

app = Flask(__name__)
app.config['SECRET_KEY'] = 'teste'
#app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False #faz com que não recebe todas as notificações feitas na BD
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///../models/instance/softpm_database.db"


#Configurações de envio de emails
app.config['MAIL_SERVER'] = 'smtp.gmail.com' 
app.config['MAIL_PORT'] = 587
app.config['MAIL_USE_TLS'] = True
app.config['MAIL_USERNAME'] = 'adicionar o email' 
app.config['MAIL_PASSWORD'] = 'adicionar a chave' # chave gerada no google 
app.config['MAIL_DEFAULT_SENDER'] = 'adicionar o email' 
mail = Mail(app)

db.init_app(app)

login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'

@login_manager.user_loader
def load_user(user_id):
    user = User.query.get(user_id)
    #print(user)
    return user

class Anonymous(AnonymousUserMixin):
  def __init__(self):
    self.name = 'Guest'
    self.authenticated = False
login_manager.anonymous_user = Anonymous

@app.route('/', methods=['GET', 'POST'])
def home():
    
    
    return render_template('login.html')


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        info = request.form
        email = info['email']
        password = info['password']

        # Buscar o usuário pelo email
        user = User.query.filter_by(email=email).first()

        if user and check_password_hash(user.password, password):
            user.authenticated = True
            db.session.commit()
            login_user(user)
            return redirect(url_for('company'))   
        else:
            flash("User ou password incorreta!")
        
    if current_user.is_authenticated:
        return redirect(url_for('home'))

    return render_template('login.html')


@app.route('/createaccount', methods=['POST'])
def create_account():
    if request.method == 'POST':
        info = request.form
        username = info['username']
        email = info['email']
        password = info['password']
        password2 = info['password2']
        company = info['company']
        print(company)

        # Verificar se todos os campos obrigatórios foram preenchidos
        if not username or not email or not password:
            flash('Todos os campos são obrigatórios!', 'error')
            return redirect(url_for('create_account'))

        # Verificar se o user já existe
        existing_user = User.query.filter_by(email=email).first()
        if existing_user:
            flash('Já existe um utilizador com este email. Tente outro.', 'error')
            return redirect(url_for('create_account'))

        # Hashear a senha
        hashed_password = generate_password_hash(password)

        # Criar a nova conta (por default,tem o role básico de"client")
        if password.strip() == password2.strip():
            new_user = User(username=username, email=email, password=hashed_password, isActive=True, role_id=2)  # role_id=2 user do tipo 'client' 
            db.session.add(new_user)
            db.session.commit()

            flash('Conta criada com sucesso! Faça o login.', 'success')
            return redirect(url_for('login'))
        else:
            flash('As passwords não coincidem! Faça o registo novamente', 'error')
            

    return render_template('login.html')

# ======== funções dos tokens recuperação de password
def generate_login_token(user_id):
    s = Serializer(app.config['SECRET_KEY'])
    return s.dumps({'user_id': user_id})

def validate_login_token(token, max_age=3600):
    s = Serializer(app.config['SECRET_KEY'])
    try:
        data = s.loads(token, max_age=max_age)
        return data['user_id']
    except:
        return None
    
def generate_token(user_id):
    s = Serializer(app.config['SECRET_KEY'])  
    return s.dumps({'user_id': user_id})

def send_recovery_email(to_email, url):
    msg = Message('Recuperação de Senha', sender='noreply@example.com', recipients=[to_email])
    msg.body = f'Para redefinir sua senha, clique no seguinte link: {url}'
    mail.send(msg)

@app.route('/account/recoverpassword',  methods=['POST'])
def recoverpassword():

    try:
        if request.method == 'POST':
            print('=============== > Entrou no POST da recuperação')
            email = request.form['recover--user']
            user = User.query.filter_by(email=email).first()
            
            if user:
                print('=============== >User existe na BD')
                token = generate_token(user.user_id)
                print('=============== >BreakPoint-1 User.user_id')
                recover_url = url_for('reset_password', token=token, _external=True)
                print('=============== >BreakPoint-2 RecoverUrl')
                send_recovery_email(user.email, recover_url)
                print('=============== >BreakPoint-3 Send Email')
                flash('Um link de recuperação foi enviado para seu e-mail.', 'success')
                
            else:
                print('=============== > Email não encontrado')
                flash('E-mail não encontrado.', 'danger')
                

            return redirect(url_for('login'))
        return redirect(url_for('login'))

    except Exception as e:
        flash(f'Ocorreu um erro com o seu pedido!', 'danger')

    return redirect(url_for('login'))


@app.route("/resetpassword/<token>", methods=['GET', 'POST'])
def reset_password(token):
    try:
        s = Serializer(app.config['SECRET_KEY'])
        data = s.loads(token,max_age=3600)  # 1 hora de validade
        user_id = data['user_id']
    except:
        flash('O link de recuperação é inválido ou expirou.', 'danger')
        return redirect(url_for('recover_password_form'))
    
    if request.method == 'POST':
        password = request.form['password']
        confirm_password = request.form['confirm_password']
        
        if password != confirm_password:
            flash('As senhas não coincidem.', 'danger')
            return redirect(url_for('reset_password', token=token))
        
        user = User.query.get(user_id)
        user.password = generate_password_hash(password)
        db.session.commit()
        flash('Sua senha foi redefinida com sucesso.', 'success')
        

    return render_template('reset_password.html')

# ======== Fim recuperação de password


@app.route('/company', methods=['GET', 'POST'])
@login_required
def company():

    #companies = []
    #dummy_companies = ['Company A', 'Company B', 'Company C']  # List of 3 companies
    companies = companies = current_user.companies  

    if request.method == 'POST':
        companySelected = request.form['company']
        print('Company Selected:', companySelected)

        # Primeiro, definir todas as empresas associadas ao current_user como isSelected = False
        for company in companies:
            company.isSelected = False

        # Em seguida, definir a empresa selecionada como isSelected = True
        selected_company = Company.query.filter_by(company_id=companySelected).first()
        if selected_company:
            selected_company.isSelected = True
        # Aplicar as alterações no banco de dados
        db.session.commit()
    
        return redirect(url_for('dashboard'))
    
    return render_template('company.html', companies=companies)

@app.route('/login/createcompany', methods=['POST'])
def create_company():
        try:
            output = request.form
            print('>>>>>>>>>>>>>>>> A Criar a Empresa... <<<<<<<<<<<<<<<<<<<<')
            print(output)

            company_name = output['company_name']
            website = output['website']
            job_position = output['job_position']
            description = output['description']
            notes =  output['description']

            print(company_name, website, job_position, description, notes)

             # Criar uma nova instância de Company
            newCompany = Company(
                company_name=company_name,
                website=website,
                job_position=job_position
            )
            newCompany.description = description
            newCompany.notes = notes

            # Adicionar a nova empresa à sessão e dar commit
            db.session.add(newCompany)
            db.session.commit()
            # Associar a nova empresa ao current_user
            current_user.companies.append(newCompany)

            # Fazer commit para salvar a associação entre o current_user e a empresa
            db.session.commit()

            flash('Empresa criada com sucesso!', 'success')
            return redirect(url_for('company'))
        
        except:
            flash('Algo aconteceu de errado, tente novamente!', 'error')
            return redirect(url_for('company'))
           
        
@app.route("/dashboard")
@login_required
def dashboard():

    #print('Utilizador: ',current_user)
    # Obter uma lista das empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
    # Obter apenas a empresa
    company = next((company for company in current_user.companies if company.isSelected), None)

    if selectedCompany:
        return render_template('dashboard/dashboard.html', company=company)
    else:
        return redirect(url_for('company'))

@app.route('/dashboard/tickets', methods=['GET', 'POST'])
@login_required
def tickets():

    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
    if selectedCompany:
        # Como o 'selectedCompany' é uma lista, obtemos o primeiro elemento (caso haja múltiplas empresas selecionadas)
        company = selectedCompany[0]
        # Obter todos os tickets relacionados aos empregados da empresa selecionada
        tickets = Ticket.query.join(Employee).filter(Employee.company_id == company.company_id).all()
        #obter todos os employees da empresa selecionada
        employees = Employee.query.filter(Employee.company_id == company.company_id).all()
        # Obter todos os status do banco de dados
        statuses = Status.query.all()
        # Obter todos os tipos do banco de dados
        types = Type.query.all()

        return render_template('dashboard/tickets.html', tickets=tickets, employees=employees, statuses=statuses, types=types)
    else:
        return redirect(url_for('company'))
    

@app.route('/ticket/create', methods=['POST'])
@login_required
def create_ticket():
    title = request.form.get('title')
    ticket_nr = request.form.get('ticket_nr')
    sprint_nr = request.form.get('sprint_nr')
    description = request.form.get('description')
    type_id = request.form.get('type_id')
    status_id = request.form.get('status_id')
    employee_id = request.form.get('employee_id')
    link = request.form.get('link')
    notes = request.form.get('notes')
    priority = request.form.get('priority')

    new_ticket = Ticket(
        ticket_nr=ticket_nr,
        title=title,
        link = link,
        description=description,
        notes = notes,
        sprint_nr=sprint_nr,
        employee_id=employee_id,  
        type_id=type_id,
        status_id=status_id,
        priority=priority
    )

    # Adicionar o novo ticket à BD
    db.session.add(new_ticket)
    db.session.commit()

    return redirect(url_for('tickets'))
  
     
@app.route('/ticket/edit/<int:ticket_id>', methods=['GET', 'POST'])
@login_required
def edit_ticket(ticket_id):
    
    ticket = Ticket.query.get_or_404(ticket_id)

    if request.method == 'POST':
        # Atualizar os dados do ticket com os valores do formulário
        ticket.title = request.form['title']
        ticket.ticket_nr = request.form['ticket_nr']
        ticket.sprint_nr = request.form['sprint_nr']
        ticket.description = request.form['description']
        ticket.type_id = request.form['type_id']
        ticket.status_id = request.form['status_id']
        ticket.notes = request.form['notes']
        ticket.employee_id = request.form['employee_id']
        ticket.priority = request.form['priority']
        
        db.session.commit()
        
        return redirect(url_for('tickets'))

    # Renderizar o template de edição de ticket com os dados atuais
    return render_template('ticket/edit_ticket.html', ticket=ticket)

@app.route('/ticket/delete/<int:ticket_id>', methods=['POST'])
@login_required
def delete_ticket(ticket_id):

    ticket = Ticket.query.get_or_404(ticket_id)

    db.session.delete(ticket)
    db.session.commit()

    return redirect(url_for('tickets'))   



@app.route('/dashboard/meetings', methods=['GET', 'POST'])
@login_required
def meetings():
     
    hasRole = current_user.role_id
    #print('ROLE do USER:', hasRole)
    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
         

    return render_template('dashboard/meetings.html')
    

@app.route('/dashboard/analytics', methods=['GET', 'POST'])
@login_required
def analytics():
     
    hasRole = current_user.role_id
    #print('ROLE do USER:', hasRole)
    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
         

    return render_template('dashboard/analytics.html')


@app.route('/dashboard/wiki', methods=['GET', 'POST'])
@login_required
def wiki():
     
    hasRole = current_user.role_id
    #print('ROLE do USER:', hasRole)
    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
         

    return render_template('dashboard/wiki.html')


@app.route('/dashboard/todo', methods=['GET', 'POST'])
@login_required
def todo():
     
    hasRole = current_user.role_id
    #print('ROLE do USER:', hasRole)
    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
         

    return render_template('dashboard/todo.html')


@app.route('/dashboard/notes', methods=['GET', 'POST'])
@login_required
def notes():
     
    hasRole = current_user.role_id
    #print('ROLE do USER:', hasRole)
    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
         

    return render_template('dashboard/notes.html')


@app.route('/dashboard/configuration', methods=['GET', 'POST'])
@login_required
def configuration():
     
    hasRole = current_user.role_id
    #print('ROLE do USER:', hasRole)
    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]

    if request.method == 'POST':
        companies = current_user.companies
        company_id = request.json.get('company_id')
        print('ID da empresa: ', company_id)
        
        # obter a empresa pelo o id
        selected_company = next((company for company in current_user.companies if str(company.company_id) == str(company_id)), None)
        print('ID da empresa2: ', selected_company)
        
        if not selected_company:
            return jsonify({'error': 'Company not found'}), 404
        
        return jsonify({
            'company_name': selected_company.company_name,
            'website': selected_company.website,
            'job_position': selected_company.job_position,
            'description': selected_company.description,
        })

    
    if selectedCompany and hasRole == 1:
        companies = current_user.companies


        return render_template('dashboard/configuration.html', companies=companies)
    else:
        return redirect(url_for('dashboard'))


@app.route('/dashboard/configuration/employees', methods=['POST'])
@login_required
def get_employees():
    company_id = request.json.get('company_id')

    # obter a empresa pelo o id
    selected_company = next((company for company in current_user.companies if str(company.company_id) == str(company_id)), None)
    print('ID da empresa2: ', selected_company)

    if not selected_company:
        return jsonify({'error': 'Company not found'}), 404

    # obter a lista de empregados
    employees = selected_company.employees  # Assuming employees is a list of objects with necessary details
    print('lista colaboradores: ', employees)

    return jsonify({
        'employees': [{'employee_id': emp.employee_id,'name': emp.name, 'position': emp.job_position, 'department':emp.department, 'notes':emp.notes} for emp in employees]
    })


@app.route('/edit_employee/<int:employee_id>', methods=['POST'])
@login_required
def edit_employee(employee_id):
    # Logic to update the employee in the database
    employee = Employee.query.get_or_404(employee_id)
    employee.name = request.form['employee_name']
    employee.position = request.form['employee_position']
    employee.department = request.form.get('employee_department')
    employee.notes = request.form.get('employee_notes')
    db.session.commit()
    flash('Employee updated successfully.')
    return redirect(url_for('configuration'))

@app.route('/delete_employee/<int:employee_id>', methods=['POST'])
@login_required
def delete_employee(employee_id):
    # Logic to delete the employee from the database
    employee = Employee.query.get_or_404(employee_id)
    
    try:
        
        db.session.delete(employee)
        db.session.commit()
        
        print('Deleted employee id: ', employee_id)
        return jsonify({'success': True, 'message': 'Colaborador eliminado com sucesso.'})
    except Exception as e:
        return jsonify({'success': False, 'message': 'An error occurred while deleting the employee.'}), 500



@app.route('/dashboard/admin', methods=['GET', 'POST'])
@login_required
def admin():
     
    hasRole = current_user.role_id
    #print('ROLE do USER:', hasRole)
    # Obter as empresas associadas ao current_user com o isSelected = True
    selectedCompany = [company for company in current_user.companies if company.isSelected]
         

    return render_template('dashboard/admin.html')

@app.route('/logout')
@login_required
def logout():
    user = current_user
    user.authenticated = False
      
    # Obter a empresa selecionada e associada ao current_user e colocar isSelected = False
    companies = companies = current_user.companies
    for company in companies:
        company.isSelected = False

    db.session.commit()

    logout_user()
    return redirect(url_for('home'))

app.static_folder = 'static'

if __name__ == "__main__":
    #chamar a BD
    with app.app_context():
        #db.create_all()

        # Função para atualizar os valores da coluna 'name' na tabela 'Status'
        '''
        try:
            # Executa múltiplas queries de atualização para cada tipo
            db.session.execute(text("UPDATE type SET name = 'User Story' WHERE type_id = 1;"))
            db.session.execute(text("UPDATE type SET name = 'Bug' WHERE type_id = 2;"))
            db.session.execute(text("UPDATE type SET name = 'Issue' WHERE type_id = 3;"))
            db.session.execute(text("UPDATE type SET name = 'Feature' WHERE type_id = 4;"))
            db.session.execute(text("UPDATE type SET name = 'Epic' WHERE type_id = 5;"))
            db.session.execute(text("UPDATE type SET name = 'Release' WHERE type_id = 6;"))
            
            db.session.commit()  # Confirma as mudanças no banco de dados
            print('Tipos atualizados com sucesso.')
        except Exception as e:
            db.session.rollback()  # Faz rollback no caso de erro
            print(f'Erro ao atualizar os tipos: {e}')
        '''

        #Adicionar uma coluna a uma tabela da BD (é necessário fazer o import: "from sqlalchemy import text", e criar a nova coluna na class da tabela)
        '''
        try:
            db.session.execute(text('ALTER TABLE ticket ADD COLUMN priority Integer NOT NULL DEFAULT "";'))
            db.session.commit()
            print('Coluna adicionada com sucesso.')
        except Exception as e:
            db.session.rollback()  # Faz o rollback no caso de erro
            print(f'Erro ao adicionar a coluna: {e}')
        '''
        '''
        try:
            # Atualiza os registros antigos com prioridade 1
            db.session.execute(text('UPDATE ticket SET priority = 1 WHERE priority != 1;'))
            db.session.commit()
            print('Registros atualizados com prioridade 1.')

        except Exception as e:
            db.session.rollback()  # Faz o rollback no caso de erro
            print(f'Erro ao adicionar a coluna ou atualizar os registros: {e}')
        '''

        #Adicionar um user
        #new_user = User(username='Claudio Coelho', email='claudiocoelho181@hotmail.com', password='teste', isActive=True, role_id=1)
        #db.session.add(new_user)
        #db.session.commit()

        # Adicionar um novo user com a senha hashed
        '''
        new_user = User(
            username='Claudio Coelho', 
            email='claudiocoelho181@hotmail.com', 
            password=generate_password_hash('teste'),  # Hashear a senha antes de armazenar
            isActive=True, 
            role_id=1
        )
        '''
        # Adicionar um novo user 
        #db.session.add(new_user)
        #db.session.commit()
        #print('User adicionado com sucesso, senha armazenada de forma segura.')

        # Procurar o user pelo email
        '''
        user = User.query.filter_by(email='claudio.ismat@gmail.com').first()
        if user:
            user.password = generate_password_hash('teste')
            db.session.commit()
            print('Senha atualizada com sucesso.')
        else:
            print('Usuário não encontrado.')
        '''

        #Adicionar um Role
        '''
        new_role = Role(master=False, support=False,client=True,role_description='User of the Application.')
        db.session.add(new_role)
        db.session.commit()
        '''
        
        # Buscar o Role com role_id=1
        '''
        role_to_delete = Role.query.get(2)

        if role_to_delete:
            db.session.delete(role_to_delete)
            db.session.commit()
            print(f'Role eliminado com sucesso.')
        else:
            print(f'Role não encontrado.')
        '''
        #Update a um Role
        '''
        role_to_update = Role.query.get(1)

        if role_to_update:
            role_to_update.support = False
            db.session.commit()
            print(f'Role com a coluna atualizada.')
        else:
            print(f'Role não encontrado.')

        '''

        #Adicionar uma Company
        #new_company = Company(company_name='Casafari',website='https://pt.casafari.com/',job_position='CRM Project Masnager')
        #db.session.add(new_company)
        #db.session.commit()

        #Atribuir uma Copmpany a um User, Adicionando uma relação na tabela userHasCompany
        #user = User.query.filter_by(username='Claudio Coelho').first() 
        #for company in user.companies:
            #print(company.company_name)
        #print(user)
        #company = Company.query.filter_by(company_name='Casafari').first()
        #print(company)
        #Adicionar a empresa ao user (cria a relação na tabela userHasCompany)
        #user.companies.append(company)
        #db.session.commit()
        #print(f'A empresa {company.company_name} foi atribuída ao usuário {user.username}.')

        # Query SQL para adicionar a coluna company_id à tabela Employee
        #alter_query = text('ALTER TABLE employee ADD COLUMN job_position VARCHAR(50);')
        #db.session.execute(alter_query)
        #db.session.commit()

        # Adicionar um novo Employee associado à empresa criada
        #company = Company.query.filter_by(company_name='Casafari').first()
        #companyid = company.company_id
        #new_employee = Employee(name='João Costa', job_position='Developer', department='CRM', company_id=1)
        #db.session.add(new_employee)
        #db.session.commit()
        #print(f'Empresa {company.company_name} e empregado {new_employee.name} foram adicionados.')


        # Buscar o Employee com employee_id=1
        #employee_to_delete = Employee.query.get(6)
        # Verificar se o employee foi encontrado
        #if employee_to_delete:
            # Deletar o employee
            #db.session.delete(employee_to_delete)
            #db.session.commit()
            #print("Employee com ID 1 deletado com sucesso.")
        #else:
            #print("Employee com ID 1 não encontrado.")

        '''
        ## CRIAR UM TICKET A ASSIGNAR AO EMPLOYEE
        # Suponha que você já tenha o id do employee (employee_id) ao qual deseja adicionar o ticket
        employee_id = 1  # ID do Employee que vai receber o ticket

        # Dados do novo ticket
        new_ticket_nr = 1234
        new_title = "Novo Ticket"
        new_link = "https://azuredevops.com/tickets/1234"
        new_description = "Este é um ticket de exemplo"
        new_notes = "Sem observações no momento"
        new_sprint_nr = 5

        # Criando uma nova instância do Ticket
        new_ticket = Ticket(
            title=new_title,
            ticket_nr=new_ticket_nr,
            link=new_link,
            description=new_description,
            notes=new_notes,
            sprint_nr=new_sprint_nr,
            employee_id=employee_id  # Relaciona o ticket ao Employee
        )

        # Adicionando o ticket ao banco de dados
        db.session.add(new_ticket)
        db.session.commit()

        print(f'Ticket {new_ticket.title} adicionado ao Employee com ID {employee_id}')
        '''

        '''
        ## ELIMINAR UM TICKET
        ticket_id = 2  
        ticket_to_delete = Ticket.query.get(ticket_id)
        # Verificar se o ticket foi encontrado
        if ticket_to_delete:
            # Deletar o ticket
            db.session.delete(ticket_to_delete)
            db.session.commit()
            print("Ticket eliminado com sucesso.")
        else:
            print("Ticket não encontrado.")
        '''

        '''
        item_to_delete = Company.query.get(2)
        # Verificar se o ticket foi encontrado
        if item_to_delete:
            # Deletar o item
            db.session.delete(item_to_delete)
            db.session.commit()
            print(" eliminado com sucesso.")
        else:
            print(" não encontrado.")
        '''

        '''
        # Criar um novo Status
        new_status = Status(new=False, inprogress=False, testing=False, done=False, standby=False, fix=True)
        # Adicionar ao banco de dados
        db.session.add(new_status)
        db.session.commit()
        '''

        '''
        # Criar um novo ticket e associar ao tipo criado
        new_ticket = Ticket(
            title="Novo Ticket",
            ticket_nr=12345,
            link="https://linkdoazuredevops.com",
            description="Descrição do novo ticket",
            notes="Notas do ticket",
            sprint_nr=12,
            employee_id=1,  # ID do Employee
            type_id=1,  # ID do tipo associado
            status_id=1 # ID do Status associado
        )

        # Adicionar ao banco de dados
        db.session.add(new_ticket)
        db.session.commit()
        '''

        
        ## FAZER DROP A TABELAS
        #Ticket.__table__.drop(db.engine)
        #Type.__table__.drop(db.engine)
        

        #db.session.add()
        #db.session.commit()
        print('ActityApp-Running')

    app.run(debug=True)
