certutil -f -p password1 -importpfx server.pfx

netsh http delete urlacl url=https://+:10052/
netsh http add urlacl url=https://+:10052/ user=%USERNAME%

netsh http delete sslcert ipport=0.0.0.0:10052
netsh http add sslcert ipport=0.0.0.0:10052 626aaeed9d29885436e3c8bf7ba7fb1fafe5316c appid={b5c62c12-b5ea-4806-b8d5-bb7a7c023d0f}

