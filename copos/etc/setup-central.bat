set service=copos
..\..\nssm stop "Evolve %service%"
..\..\nssm remove "Evolve %service%" confirm
..\..\nssm install "Evolve %service%" "c:\current\%service%\run-svr.bat"
..\..\nssm set "Evolve %service%" Start SERVICE_DELAYED_AUTO_START
..\..\nssm start "Evolve %service%"
