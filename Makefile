
all: cliente servidor


cliente:
	jar -cvfe Cliente.jar io.github.oscarmaestre.jsimplechat.Cliente -C JSimpleChat\build\classes .
	
servidor:
	jar -cvfe Servidor.jar io.github.oscarmaestre.jsimplechat.Servidor -C JSimpleChat\build\classes .