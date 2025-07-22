
Esta es la primera entrega del Proyecto de programación 24/25 del grupo 43.5 de prop. 
El cuál esta formado por:

Alex Lafuente Gonzalez (correo: alex.lafuente.gonzalez@estudiantat.upc.edu)
Christian Conejo Raubie (correo: christian.conejo@estudiantat.upc.edu)
Dani Mejias Cerezo (correo: dmejias@estudiantat.upc.edu)
Pol Garcia Parra (correo: pol.garcia.parra@estudiantat.upc.edu)


----OBJETIVO-------------------------------------------------------------------------------------------------------------------------------------------------------------------- 
 
El objetivo es desarrollar un sistema para optimizar la distribución de productos en un supermercado, 
maximizando la probabilidad de compra de los clientes mediante la colocación estratégica de los productos 
según sus relaciones o similitudes.


-----NUESTRO SISTEMA-------------------------------------------------------------------------------------------------------------------------------------------------------------------- 

Nuestro sistema se encarga de Gestionar Productos. Asi como la gestión de similitudes entre productos. 
Para el cálculo de la solución óptima utilizamos 2 algoritmos uno por fuerza bruta y otro voraz. 
Una vez calculada la solución es possible la modificacion de esta. 

En esta primera entrega como funcionalidad extra y para darle mas realismo al sistema hemos implementado una gestión de usuarios.

----NAVEGA POR NUESTRO PROYECTO-------------------------------------------------------------------------------------------------------------------------------------------------------------------- 

Entrega 1/
├── Data/
│   ├──Usuarios/ -> Carpeta donde se encuentran las carpetas de persistencia asociadas a cada usuario. En estas están los archivos JSON dedicados a almacenar informacion del estado del sistema.
│   └── input_users.txt -> Fichero el cual contiene los usuarios y contraseñas registrados en el sistema
├── Doc/
│   └── documentación_del_proyecto
├── EXE/ - > En esta carpeta se encuentran todos los ficheros ejecutables
│
├── Font/
│   ├── classes/
│   │   └── *.java (clases del proyecto)
│   ├── datacontrollers/
│   │   └── *.java (controladores de persistencia)
│   ├── domaincontrollers/
│   │   └── *.java (controladores de dominio)
│   ├── Exceptions/
│   │   └── *.java (clases de excepciones)
│   ├── forms
│   │   └── *.java y *.form (Dedicados al diseño de las vistas)
│   ├── presentation/
│   │   ├── image_src -> Carpeta donde se encuentran todas las imagenes recurso del sistema
│   │   │
│   │   └── *.java (controladores de presentación y clases auxiliares de presentacion)
│   ├── Tests/
│   │   └── *.java (test del proyecto)
│   ├── Main.java
│   │
│   ├── MainVisual.java
│   │
│   ├── manifest_visual.txt
│   │
│   └── manifest.txt
├── lib/ 
│
└── makefile

Consulta los index.txt que encontraras en cada carpeta para tener información mas detallada

----COMO EJECUTAR-------------------------------------------------------------------------------------------------------------------------------------------------------------------- 

En el directorio en el que se encuentra este archivo encontraras un makefile. 
Con el que podrás ejecutar desde una terminal situandote en esta carpeta:

- make  all -
Crea el directorio de salida para los .class en la carpeta exe si es necessario y
compila todos los archivos fuente. 

- make jars - 
Compila el proyecto como all, crea el fichero ejecutable MainProgram.jar.

- make jars-visual -
Compila el proyecto como all, crea el fichero ejecutable MainVisualProgram.jar.

- make execute- (Despues de ejecutar make jars)
Ejecuta el fichero MainProgram.jar

- make execute-visual -
Ejecuta el fichero MainVisualProgram.jar

- make fulltest -
Compila todo el codigo fuente incluido las pruebas JUnit y ejecuta todos los test.

- make run - (Despues de ejecutar make all i make jars)
El programa ejecuta los juegos de prueba. 

Warning! Por alguna razon falla por lo tanto  hay que ejecutar los juegos de prueba a mano. 
En esta misma carpeta se encuentran los juegos de prueba y su descripcion. Para ejecutar el juego de 
prueba hacer make execute y pegar el imput del juego de pruebas.

- make clean -
Elimina todos los archivos .class generados



