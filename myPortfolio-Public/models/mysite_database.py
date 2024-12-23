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

class Project(db.Model):
    project_id = db.Column(db.Integer, primary_key=True) 
    name = db.Column(db.VARCHAR(100), nullable=False)  
    icon = db.Column(db.VARCHAR(255)) 
    description = db.Column(db.TEXT)  
    technologies = db.Column(db.VARCHAR(255))  
    github = db.Column(db.VARCHAR(255))  
    link = db.Column(db.VARCHAR(255)) 
    type = db.Column(db.Integer)  # 1-Cyber | 2-WebApp | 3-Algorithmics
    photo1 = db.Column(db.VARCHAR(255)) 
    photo2 = db.Column(db.VARCHAR(255))  
    photo3 = db.Column(db.VARCHAR(255))  

    def __init__(self, name, icon=None, description=None, technologies=None, github=None, link=None, type=None, photo1=None, photo2=None, photo3=None):
        self.name = name
        self.icon = icon
        self.description = description
        self.technologies = technologies
        self.github = github
        self.link = link
        self.type = type
        self.photo1 = photo1
        self.photo2 = photo2
        self.photo3 = photo3

    def to_json(self):
        return {
            "project_id": self.project_id,
            "name": self.name,
            "icon": self.icon,
            "description": self.description,
            "technologies": self.technologies,
            "github": self.github,
            "link": self.link,
            "type": self.type,
            "photo1": self.photo1,
            "photo2": self.photo2,
            "photo3": self.photo3
        }

    def __repr__(self):
        return f'<Project Name={self.name}>'



mapper_registry.configure()
