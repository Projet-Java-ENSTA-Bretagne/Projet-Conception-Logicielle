#!/usr/bin/env python
# -*- coding: utf-8 -*-

import socket

host = "ieta-docs.ddns.net"
port = 60000

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((host, port))
client.send(b"{'command': 'PING'}\n")

response = client.recv(4096)
print(response)
