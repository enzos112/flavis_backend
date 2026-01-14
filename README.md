# 🍪 Flavis Cookies - Backend API
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot&logoColor=white)](https://spring.io/)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)

Núcleo lógico del ecosistema **Flavis Cookies**. Esta API gestiona el inventario de galletas, el procesamiento de pedidos con comprobantes y la analítica de clientes basada en su inversión total.

## 🛠️ Tecnologías Principales
* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.x (JPA, Security, Web)
* **Base de Datos:** MySQL 8.x
* **Media:** Cloudinary SDK (Gestión de Vouchers y Catálogo)
* **Gestor:** Maven

## ⚙️ Configuración del Entorno
Antes del primer arranque, es obligatorio configurar las siguientes **Variables de Entorno** en IntelliJ IDEA (Edit Configurations -> Environment Variables):

### 🗄️ Base de Datos (MySQL)
* `DB_URL`: `jdbc:mysql://localhost:3306/flavis_db`
* `DB_USERNAME`: `tu_usuario`
* `DB_PASSWORD`: `tu_contraseña`

### ☁️ Cloudinary (Cuenta oficial de la marca)
* `CLOUDINARY_CLOUD_NAME`: `nombre_del_cloud`
* `CLOUDINARY_API_KEY`: `tu_api_key`
* `CLOUDINARY_API_SECRET`: `tu_api_secret`

## 🚀 Instalación y Arranque
1. Asegúrate de tener instalado **MySQL Server** y haber creado la base de datos `flavis_db`.
2. Clona el repositorio:
   ```bash
   git clone [https://github.com/tu-usuario/flavis-backend.git](https://github.com/tu-usuario/flavis-backend.git)


   Configura las variables mencionadas arriba en IntelliJ.

Ejecuta el proyecto:

Bash

mvn spring-boot:run
La API correrá por defecto en: http://localhost:8080/api
