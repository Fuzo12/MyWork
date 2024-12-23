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
from models.mysite_database import User, Role, Project, db #importa a tabela user da BD

app = Flask(__name__)
app.config['SECRET_KEY'] = 'mysecretpassisateste'
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///../models/instance/mysite_database.db"


#Configurações de envio de emails
app.config['MAIL_SERVER'] = 'smtp.gmail.com' 
app.config['MAIL_PORT'] = 587
app.config['MAIL_USE_TLS'] = True
app.config['MAIL_USERNAME'] = 'add email' 
app.config['MAIL_PASSWORD'] = 'add email key' 
app.config['MAIL_DEFAULT_SENDER'] = 'add email' 
mail = Mail(app)


db.init_app(app)

login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'

@login_manager.user_loader
def load_user(user_id):
    user = User.query.get(user_id)
    print(user)
    return user

class Anonymous(AnonymousUserMixin):
  def __init__(self):
    self.name = 'Guest'
    self.authenticated = False
login_manager.anonymous_user = Anonymous

@app.route('/', methods=['GET', 'POST'])
def home():

    cyberProjects = Project.query.filter_by(type=1).all()
    webAppProjects = Project.query.filter_by(type=2).all()
    algorithmicsProjects = Project.query.filter_by(type=3).all()
    dataScienceProjects = Project.query.filter_by(type=4).all()
    AIProjects = Project.query.filter_by(type=5).all()
    
    return render_template('index.html', cyberProjects=cyberProjects, 
                           webAppProjects=webAppProjects,
                           algorithmicsProjects=algorithmicsProjects,
                           dataScienceProjects=dataScienceProjects, 
                           AIProjects=AIProjects)


@app.route('/project-details/<int:project_id>')
def project_details(project_id):
    print(project_id)
    project = Project.query.filter_by(project_id=project_id).first()
    if project:
        print(project.technologies)
        return jsonify({
            'name': project.name,
            'description': project.description,
            'technologies': project.technologies,
            'github': project.github,
            'link': project.link,
            'photo1': project.photo1


        })
    return jsonify({'error': 'Projeto não encontrado'}), 404

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        info = request.form
        email = info['email']
        password = info['password']

        #obter o user pelo email
        user = User.query.filter_by(email=email).first()

        if user and check_password_hash(user.password, password):
            user.authenticated = True
            db.session.commit()
            login_user(user)
            return redirect(url_for('dashboard'))   
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


@app.route('/contact-form', methods=['POST'])
def submit_form():
    first_name = request.form.get('first_name')
    last_name = request.form.get('last_name')
    email = request.form.get('email')
    message = request.form.get('message')

    try:
        # Configurar o e-mail
        msg = Message(
            subject='Nova Mensagem do Formulário de Contato do site ClaudioCoelho',
            sender=app.config['MAIL_USERNAME'],
            recipients=['claudiocoelho181@hotmail.com']
        )

        # Corpo do e-mail
        msg.body = f"""
        Você recebeu uma nova mensagem do formulário de contato do site ClaudioCoelho:

        Nome: {first_name} {last_name}
        E-mail: {email}

        Mensagem:
        {message}
        """

        # Enviar o e-mail
        mail.send(msg)
        flash('Message sent. Thanks!', 'success')  
    except Exception as e:
        print(f'Error sending the e-mail: {e}')
        flash('Error sending the e-mail.', 'danger')  

    return redirect(url_for('home') + '#contact')


@app.route("/dashboard")
@login_required
def dashboard():

    return render_template('dashboard/dashboard.html')

@app.route('/dashboard/tickets', methods=['GET', 'POST'])
def tickets():
    
    
     return render_template('dashboard/tickets.html')


@app.route('/logout')
@login_required
def logout():
    user = current_user
    user.authenticated = False
    db.session.commit()
    logout_user()
    return redirect(url_for('home'))

app.static_folder = 'static'

if __name__ == "__main__":
    #chamar a BD
    with app.app_context():
        #db.create_all()

        #db.session.commit()
        print('ActityApp-Running')

    app.run(debug=True)
