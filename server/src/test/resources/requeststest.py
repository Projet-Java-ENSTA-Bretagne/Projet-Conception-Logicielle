#!/usr/bin/env python
# -*- coding: utf-8 -*-

import socket

host = "localhost"
port = 6666

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((host, port))
client.send(b"{'command': 'PING'}\n")

response = client.recv(4096)
print(response)
