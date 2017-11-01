#!/bin/sh
HOME=http://localhost:8080
curl -X GET $HOME/admins
echo ""
curl -X POST -H "Content-Type: application/json" -d "{\"email\":\"asdf@asdf.com\",\"hashPassword\":\"pwdpwdpwd\",\"token\":\"tokenitis\",\"firstName\":\"Daniel\",\"lastName\":\"Soh\",\"isSuperAdmin\":true }" $HOME/admins/
echo ""
curl -X GET $HOME/admins
echo ""
curl -X GET -H "Content-Type: application/json" -d "{\"email\":\"asdf@asdf.com\",\"hashPassword\":\"pwdpwdpwd\"}" $HOME/log/in
