set service=copos
..\..\nssm stop "Evolve %service%"
..\..\nssm remove "Evolve %service%" confirm
..\..\nssm install "Evolve %service%" "c:\centerstage-services\%service%\run-test-all-local.bat"
..\..\nssm set "Evolve %service%" Start SERVICE_DELAYED_AUTO_START
..\..\nssm start "Evolve %service%"
