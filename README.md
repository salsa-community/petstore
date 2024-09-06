# Instrucciones para crear e instalar una arquitectura orientada a microservicios utilizando JHipster


## Pasos 1

En una ruta de nuestro equipo, crear las carpetas `petstore/gateway`, `petstore/mascotams`, `petstore/alimentoms`

```shell
mkdir -p petstore/{gateway,mascotams,alimentoms}
```

**gateway**: Aquí se coloca el proyecto que representa al gateway de nuestra arquitectura
**mascotams**: Es el microservicio que gestionará a la entidad mascotas de nuestra tienda de mascotas
**alimentoms**: Es el microservicio que gestionará a la entidad alimento de nuestra tienda de mascotas

## Paso 2 Creación del proyecto Gateway

```shell
cd petstore/gateway
jhipster
```
En el menú de JHipster, seleccionar las siguiente opciones:
```shell
? What is the base name of your application? **gateway**
? Which *type* of application would you like to create? **Gateway application**
? What is your default Java package name? **mx.infotec.gateway**
? Would you like to use Maven or Gradle for building the backend? **Maven**
? As you are running in a microservice architecture, on which port would like your server to run? It should be unique to avoid port conflicts. **8080**
? Which service discovery server do you want to use? **Consul (recommended)**
? Which *type* of authentication would you like to use? **JWT authentication (stateless, with a token)**
? Besides JUnit, which testing frameworks would you like to use? **Ninguno**
? Which *type* of database would you like to use? **MongoDB**
? Which cache do you want to use? (Spring cache abstraction) **No cache - Warning, when using an SQL database, this will disable the Hibernate 2nd level cache!**
? Which other technologies would you like to use? **Ninguno**
? Which *framework* would you like to use for the client? **Vue**
? Do you want to enable *microfrontends*? **No**
? Besides Jest/Vitest, which testing frameworks would you like to use? **Ninguno**
? Do you want to generate the admin UI? **Yes**
? Would you like to use a Bootswatch theme (https://bootswatch.com/)? **Cosmo**
? Choose a Bootswatch variant navbar theme (https://bootswatch.com/)? **Primary**
? Would you like to enable internationalization support? **Yes**
? Please choose the native language of the application **Spanish**
? Please choose additional languages to install **English**
```

## Paso 3 Corregir la configuración de correo electrónico

Para corregir la configuración de correo electrónico, agregar el siguiente método que define una configuración por default del `JavaMailSender` en el archivo `src/main/java/mx/infotec/gateway/config/WebConfigurer.java`


```java
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

...
...
...

@Bean
public JavaMailSender getJavaMailSender() {
	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	mailSender.setHost("smtp.gmail.com");
	mailSender.setPort(587);
	mailSender.setUsername("my.gmail@gmail.com");
	mailSender.setPassword("password");
	Properties props = mailSender.getJavaMailProperties();
	props.put("mail.transport.protocol", "smtp");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.debug", "true");
	return mailSender;
}
```


## Paso 4 Corregir la configuración de Spring Cloud

Reemplazar el contenido del archivo `src/main/resources/config/bootstrap.yml` con el siguiente contenido:

```yml
# ===================================================================
# Spring Cloud Consul Config bootstrap configuration for the "dev" profile
# In prod profile, properties will be overwritten by the ones defined in bootstrap-prod.yml
# ===================================================================

spring:
  application:
    name: gateway
  #profiles:
    # The commented value for `active` can be replaced with valid Spring profiles to load.
    # Otherwise, it will be filled in by maven when building the JAR file
    # Either way, it can be overridden by `--spring.profiles.active` value passed in the commandline or `-Dspring.profiles.active` set in `JAVA_OPTS`
  #  active: '@spring.profiles.active@'
  cloud:
    consul:
      config:
        fail-fast: false # if not in "prod" profile, do not force to use Spring Cloud Config
        format: yaml
        profile-separator: '-'
      discovery:
        tags:
          - profile=${spring.profiles.active}
          - version='@project.version@'
          - git-version=${git.commit.id.describe:}
          - git-commit=${git.commit.id.abbrev:}
          - git-branch=${git.branch:}
          - context-path=${server.servlet.context-path:}

      host: localhost
      port: 8500
```

## Paso 5 Levantar el servicio de Consul y la base de datos Mongodb 

Para levantar el servicio de consul tenemos que ejecutar el siguiente comando:

```shell
docker compose -f src/main/docker/consul.yml up -d
```

Para levantar el servicio de mongodb tenemos que ejecutar el siguiente comando:

```shell
docker compose -f src/main/docker/mongodb.yml up -d
```

## Paso 6 Levantar el proyecto Gateway

Abre una console y ejecuta el siguiente comando para levantar el proyecto **backend** del gateway:
```shell
./mvnw
``` 

Abre otra consola y ejecuta el siguiente comando para levanta el proyecto **front-end** en modeo desarrollo:

```shell
npm start
``` 

## Paso 7 Crear el microservicio mascotams

Nos cambiamos a la carpeta `petstore/mascotams` y ejecutamos el comando `jhipster`

```shell
cd petstore/mascotams
jhipster
``` 


En el menú de JHipster, seleccionar las siguiente opciones:

```shell
? What is the base name of your application? **mascotams**
? Which *type* of application would you like to create? **Microservice application**
? What is your default Java package name? mx.infotec.mascotams
? Would you like to use Maven or Gradle for building the backend? **Maven**
? Do you want to make it reactive with Spring WebFlux? **Yes**
? As you are running in a microservice architecture, on which port would like your server to run? It should be unique to avoid port conflicts. **8081**
? Which service discovery server do you want to use? **Consul (recommended)**
? Which *type* of authentication would you like to use? **JWT authentication (stateless, with a token)**
? Besides JUnit, which testing frameworks would you like to use? **Ninguno** 
? Which *type* of database would you like to use? **MongoDB**
? Which cache do you want to use? (Spring cache abstraction) **No cache - Warning, when using an SQL database, this will disable the Hibernate 2nd level cache!**
? Which other technologies would you like to use? **Ninguno**
? Which *framework* would you like to use as microfrontend? **No client**
? Would you like to enable internationalization support? **Yes**
? Please choose the native language of the application **Spanish**
? Please choose additional languages to install **English**
```

## Paso 8 Corregir la configuración de Spring cloud

Reemplazar el contenido del archivo `src/main/resources/config/bootstrap.yml` con el siguiente contenido:

```yml
# ===================================================================
# Spring Cloud Consul Config bootstrap configuration for the "dev" profile
# In prod profile, properties will be overwritten by the ones defined in bootstrap-prod.yml
# ===================================================================

spring:
  application:
    name: gateway
  #profiles:
    # The commented value for `active` can be replaced with valid Spring profiles to load.
    # Otherwise, it will be filled in by maven when building the JAR file
    # Either way, it can be overridden by `--spring.profiles.active` value passed in the commandline or `-Dspring.profiles.active` set in `JAVA_OPTS`
  #  active: '@spring.profiles.active@'
  cloud:
    consul:
      config:
        fail-fast: false # if not in "prod" profile, do not force to use Spring Cloud Config
        format: yaml
        profile-separator: '-'
      discovery:
        tags:
          - profile=${spring.profiles.active}
          - version='@project.version@'
          - git-version=${git.commit.id.describe:}
          - git-commit=${git.commit.id.abbrev:}
          - git-branch=${git.branch:}
          - context-path=${server.servlet.context-path:}

      host: localhost
      port: 8500
```
## Paso 9 Levantar el microservicio mascotams


Levantamos el proyecto en el puerto `8081`

```shell
SERVER_PORT=8081 ./mvnw
``` 

## Paso 10 Crear el microservicio alimentoms

Nos cambiamos a la carpeta `petstore/alimentoms` y ejecutamos el comando `jhipster`

```shell
cd petstore/alimentoms
jhipster
``` 

En el menú de JHipster, seleccionar las siguiente opciones:

```shell
? What is the base name of your application? **alimentoms**
? Which *type* of application would you like to create? **Microservice application**
? What is your default Java package name? mx.infotec.mascotams
? Would you like to use Maven or Gradle for building the backend? **Maven**
? Do you want to make it reactive with Spring WebFlux? **Yes**
? As you are running in a microservice architecture, on which port would like your server to run? It should be unique to avoid port conflicts. **8081**
? Which service discovery server do you want to use? **Consul (recommended)**
? Which *type* of authentication would you like to use? **JWT authentication (stateless, with a token)**
? Besides JUnit, which testing frameworks would you like to use? **Ninguno** 
? Which *type* of database would you like to use? **MongoDB**
? Which cache do you want to use? (Spring cache abstraction) **No cache - Warning, when using an SQL database, this will disable the Hibernate 2nd level cache!**
? Which other technologies would you like to use? **Ninguno**
? Which *framework* would you like to use as microfrontend? **No client**
? Would you like to enable internationalization support? **Yes**
? Please choose the native language of the application **Spanish**
? Please choose additional languages to install **English**
```

## Paso 11 Corregir la configuración de Spring cloud

Reemplazar el contenido del archivo `src/main/resources/config/bootstrap.yml` con el siguiente contenido:

```yml
# ===================================================================
# Spring Cloud Consul Config bootstrap configuration for the "dev" profile
# In prod profile, properties will be overwritten by the ones defined in bootstrap-prod.yml
# ===================================================================

spring:
  application:
    name: gateway
  #profiles:
    # The commented value for `active` can be replaced with valid Spring profiles to load.
    # Otherwise, it will be filled in by maven when building the JAR file
    # Either way, it can be overridden by `--spring.profiles.active` value passed in the commandline or `-Dspring.profiles.active` set in `JAVA_OPTS`
  #  active: '@spring.profiles.active@'
  cloud:
    consul:
      config:
        fail-fast: false # if not in "prod" profile, do not force to use Spring Cloud Config
        format: yaml
        profile-separator: '-'
      discovery:
        tags:
          - profile=${spring.profiles.active}
          - version='@project.version@'
          - git-version=${git.commit.id.describe:}
          - git-commit=${git.commit.id.abbrev:}
          - git-branch=${git.branch:}
          - context-path=${server.servlet.context-path:}

      host: localhost
      port: 8500
```
## Paso 9 Levantar el microservicio mascotams


Levantamos el proyecto en el puerto `8081`

```shell
SERVER_PORT=8081 ./mvnw
``` 


## Paso 10 Generación de los modelos de dominio Mascota, Alimento tanto en el Gateway y en los microservicios

Para el proyecto `alimentoms` crear, en la raiz del proyecto, el archivo `alimento.jdl` con el siguiente contenido:

```java

entity Alimento {
	nombre String required minlength(3) maxlength(50)
    precio Float required
    descripcion TextBlob
    foto ImageBlob
}

// Set pagination options
paginate Alimento with pagination
//paginate Job with pagination

// Use Data Transfer Objects (DTO)
dto * with mapstruct

// Set service options to all except few
service all with serviceImpl

// Set an angular suffix jhipster
//angularSuffix * with petStore
```

Para el proyecto `mascotams` crear, en la raiz del proyecto, el archivo `mascota.jdl` con el siguiente contenido:

```java
entity Mascota {
	nombre String required minlength(3) maxlength(50)
    edad Integer required max(200)
    precio Float required
    fechaNacimiento LocalDate
    foto ImageBlob
}

// Set pagination options
paginate Mascota with infinite-scroll
//paginate Job with pagination

// Use Data Transfer Objects (DTO)
dto * with mapstruct

// Set service options to all except few
service all with serviceImpl

// Set an angular suffix jhipster
//angularSuffix * with petStore
```

A continuación, realizar la generación de código para los dos microservicios con los siguientes comandos:

```bash
cd petstore/mascotams
jhipster import-jdl mascota.jdl
```

```bash
cd petstore/alimentoms
jhipster import-jdl alimento.jdl
```

La intefaz gráfica para acceder a los modelos de dominio de `Mascota` y `Alimento` se hace a en el proyecto del Gateway de la siguiente manera:


```bash
cd petstore/gateway
jhipster entity Alimento
```

A continuación, JHipster nos preguntará si queremos crear esta entidad a partir de una existente en algún microservicio. Le vamos a decir que sí y le vamos a pasar la ruta absoluta en donde se encuentra el microservicio que contiene la entidad Mascota, en este caso, la ruta absoluta para el microservicios es: `/home/asterix/petstore/alimentoms` (tienes que poner la ruta en donde guardaste el microservicio).

```bash
cd petstore/gateway
jhipster entity Mascota
```

A continuación, JHipster nos preguntará si queremos crear esta entidad a partir de una existente en algún microservicio. Le vamos a decir que sí y le vamos a pasar la ruta absoluta en donde se encuentra el microservicio que contiene la entidad Mascota, en este caso, la ruta absoluta para el microservicios es: `/home/asterix/petstore/mascotaoms` (tienes que poner la ruta en donde guardaste el microservicio).


Al finalizar los pasos anteriores, tenemos que volver a levantar los tres proyectos, `gateway`, `alimentoms` y `mascotams`

```shell
cd petstore/mascotams
SERVER_PORT=8081 ./mvnw

cd petstore/alimentoms
SERVER_PORT=8082 ./mvnw


cd petstore/gateway
./mvnw
```

Para poder ver la última versión de las pantallas que se han generado, se tiene que utilizar el comando npm start en el proyecto del `Gateway`

```shell
cd petstore/gateway
.npm start
```

Al finalizar el proceso, puedes acceder a la URL `http://localhost:9000` y revisar el aplicativo.

Asegurate que todos los servicios estén levantados y registrados en Consul `http://localhost:8500`