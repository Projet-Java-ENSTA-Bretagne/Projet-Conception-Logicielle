#!/usr/bin/env python
# -*- coding: utf-8 -*-

import socket

host = "127.0.0.1"
port = 6666

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((host, port))

req = """{
          'command': 'getUserByID',
          'data': {
              'id': '45fabc9a-83f2-4069-83ad-529740094efc'
          }
      }"""
req = req.replace("\n", "")
print("Sending : ", req)
client.send(req.encode() + b"\n")

response = client.recv(4096)
print(response)
