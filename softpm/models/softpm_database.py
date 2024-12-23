from flask import Flask, render_template,request
#import para a url da api
import urllib.request,json
#importar o Slq Alchemy
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import event
from sqlalchemy.orm import declarative_base, registry, relationship
from sqlalchemy import create_engine, Column, Integer, String, ForeignKey


db = SQLAlchemy()

mapper_registry = registry()


# Tabela de Associação entre User e Company
userHasCompany = db.Table('userHasCompany',
    db.Column('user_id', db.Integer, db.ForeignKey('user.user_id'), primary_key=True),
    db.Column('company_id', db.Integer, db.ForeignKey('company.company_id'), primary_key=True)
)

class User(db.Model):
    user_id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.VARCHAR(30))
    password = db.Column(db.VARCHAR(60))
    email = db.Column(db.VARCHAR(50))
    authenticated = db.Column(db.BOOLEAN, default=False)
    isActive = db.Column(db.BOOLEAN, default=False)

    # Chave estrangeira para associar com Role
    role_id = db.Column(db.Integer, db.ForeignKey('role.role_id'))

    # Definir o relacionamento com Role
    role = db.relationship('Role', backref='users')

    # Relacionamento muitos-para-muitos com a tabela Company via userHasCompany
    companies = db.relationship('Company', secondary=userHasCompany, backref='users_associated')

    def to_json(self):
        return {"username":self.username,"Email": self.email, "password": self.password}
    def is_authenticated(self):
        return self.authenticated
    def is_active(self):
        return True
    def is_anonymous(self):
        return self.isActive
    def get_id(self):
        return str(self.user_id)

    # Metodo Construtor
    def __init__(self,username,password,email,authenticated=False,isActive=False,  role_id=None):
        self.username = username
        self.password = password
        self.email = email
        self.role_id = role_id
        self.authenticated = authenticated
        self.isActive = isActive


    def __repr__(self):
        return f'<Username={self.username}>'


class Role(db.Model):
    role_id = db.Column(db.Integer, primary_key=True)
    master = db.Column(db.BOOLEAN, default=False)
    support = db.Column(db.BOOLEAN, default=False)
    client = db.Column(db.BOOLEAN, default=False)
    role_description =  db.Column(db.TEXT)
    

    def get_master(self):
        return str(self.master)
    def get_role_id(self):
        return str(self.role_id)
    def get_role_description(self):
        return str(self.role_description)

    # Metodo Construtor
    def __init__(self, master=False, support=False, client=False, role_description=''):
            self.master = master
            self.support = support
            self.client = client
            self.role_description = role_description

    def __repr__(self):
        return f'<Role={self.role_description}>'
    

class Company(db.Model):
    company_id = db.Column(db.Integer, primary_key=True)
    company_name = db.Column(db.VARCHAR(60))
    website = db.Column(db.VARCHAR(60))
    job_position = db.Column(db.VARCHAR(50))
    logo =  db.Column(db.VARCHAR(60))
    description = db.Column(db.TEXT)
    isSelected = db.Column(db.BOOLEAN, default=False)
    isActive = db.Column(db.BOOLEAN, default=True)
    notes = db.Column(db.TEXT)

    # Relacionamento muitos-para-muitos com a tabela User via userHasCompany
    users = db.relationship('User', secondary=userHasCompany, backref='companies_associated')

    # Relacionamento um-para-muitos: uma empresa pode ter vários empregados
    employees = db.relationship('Employee', backref='company', lazy=True)

    def to_json(self):
        return {"company_name":self.company_name,"job_position": self.job_position, "password": self.website, "is_active": self.isActive}
    def is_selected(self):
        return self.isSelected
    def is_active(self):
        return self.isActive
    def get_id(self):
        return str(self.company_id)

    # Metodo Construtor
    def __init__(self,company_name,website,job_position):
        self.company_name = company_name
        self.website = website
        self.job_position = job_position

    def __repr__(self):
        return f'{self.company_name}'
    

class Employee(db.Model):
    employee_id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.VARCHAR(50))
    job_position = db.Column(db.VARCHAR(50))
    department = db.Column(db.VARCHAR(50))
    notes = db.Column(db.TEXT)

    # Chave estrangeira para associar Employee com Company
    company_id = db.Column(db.Integer, db.ForeignKey('company.company_id'), nullable=False)


    def to_json(self):
        return {"name":self.name,"job_position": self.job_position, "department": self.department, "notes": self.notes}
    def get_job_position(self):
        return self.job_position
    def get_name(self):
        return self.name
    def get_id(self):
        return str(self.employee_id)

    # Metodo Construtor
    def __init__(self,name,job_position, department, company_id):
        self.name = name
        self.job_position = job_position
        self.department = department
        self.company_id = company_id

    def __repr__(self):
        return f'<Employee={self.name}>'
    

class Ticket(db.Model):
    ticket_id = db.Column(db.Integer, primary_key=True)
    ticket_nr = db.Column(db.Integer) # consiste no número do ticket no AzureDevops
    title = db.Column(db.VARCHAR(60))
    link = db.Column(db.VARCHAR(60))
    description = db.Column(db.TEXT)
    notes = db.Column(db.TEXT)
    sprint_nr = db.Column(db.Integer)
    priority = db.Column(db.Integer)

    # Relacionamento com a tabela Employee de um-para-muitos
    employee_id = db.Column(db.Integer, db.ForeignKey('employee.employee_id'), nullable=False)

    # Relacionamento bidirecional com Employee
    employee = db.relationship('Employee', backref=db.backref('tickets', lazy=True))

    # Relacionamento um-para-um com Type, agora com chave estrangeira type_id
    type_id = db.Column(db.Integer, db.ForeignKey('type.type_id'), nullable=False)
    type = db.relationship('Type', backref='ticket')

     # Relacionamento um-para-um com Type, agora com chave estrangeira status_id
    status_id = db.Column(db.Integer, db.ForeignKey('status.status_id'), nullable=False)
    status = db.relationship('Status', backref='ticket')

    def to_json(self):
        return {"ticket_id":self.ticket_id,"title": self.title, "link": self.link, "description": self.description, "sprint_nr": self.sprint_nr, "type_id": self.type_id, "status_id": self.status_id, "priority":self.priority}

    def get_id(self):
        return str(self.ticket_id)
    
    def get_title(self):
        return str(self.title)

    # Metodo Construtor
    def __init__(self,title,ticket_nr,link, description, notes, sprint_nr,employee_id, type_id, status_id, priority):
        self.title = title
        self.ticket_nr = ticket_nr
        self.link = link
        self.description = description
        self.notes = notes
        self.sprint_nr = sprint_nr
        self.employee_id = employee_id
        self.type_id = type_id
        self.status_id = status_id
        self.priority = priority

    def __repr__(self):
        return f'<title={self.title}><type={self.type_id}>'
    
# Esta classe consite em atribuir um tipo ao Ticket 
class Type(db.Model):
    name = db.Column(db.String(50), nullable=False)
    type_id = db.Column(db.Integer, primary_key=True)
    us = db.Column(db.BOOLEAN, default=False)
    bug = db.Column(db.BOOLEAN, default=False)
    issue = db.Column(db.BOOLEAN, default=False)
    feature = db.Column(db.BOOLEAN, default=False)
    epic = db.Column(db.BOOLEAN, default=False)
    release = db.Column(db.BOOLEAN, default=False)


    def to_json(self):
        return {"type_id":self.type_id}

    def get_id(self):
        return str(self.type_id)
    
    def get_type_name(self):
        return self.name

    # Metodo Construtor
    def __init__(self,name=name, us=False, bug=False, issue=False, feature=False, epic=False, release=False):
        self.name = name
        self.us = us
        self.bug = bug
        self.issue = issue
        self.feature = feature
        self.epic = epic
        self.release = release


    def __repr__(self):
        return f'<type_id={self.type_id}>'

# Esta classe consite em atribuir um Estado ao Ticket 
class Status(db.Model):
    name = db.Column(db.String(50), nullable=False)
    status_id = db.Column(db.Integer, primary_key=True)
    new = db.Column(db.BOOLEAN, default=False)
    inprogress = db.Column(db.BOOLEAN, default=False)
    testing = db.Column(db.BOOLEAN, default=False)
    done = db.Column(db.BOOLEAN, default=False)
    standby = db.Column(db.BOOLEAN, default=False)
    fix = db.Column(db.BOOLEAN, default=False)


    def to_json(self):
        return {"status_id":self.status_id}

    def get_id(self):
        return str(self.status_id)
    
    def get_status_name(self):
        return self.name

    # Metodo Construtor
    def __init__(self, name=name, new=False, inprogress=False, testing=False, done=False, standby=False, fix=False):
        self.name = name
        self.new = new
        self.inprogress = inprogress
        self.testing = testing
        self.done = done
        self.standby = standby
        self.fix = fix


    def __repr__(self):
        return f'<status_id={self.status_id}>'

mapper_registry.configure()
