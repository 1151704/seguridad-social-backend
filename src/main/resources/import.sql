
INSERT INTO sspt_rol (descripcion,rol) VALUES ('Administrador','ROLE_ADMIN');
INSERT INTO sspt_rol (descripcion,rol) VALUES ('Gerente','ROLE_GERENTE');
INSERT INTO sspt_rol (descripcion,rol) VALUES ('Usuario','ROLE_USER');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Registro civil','RC');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Tarjeta de identidad','TI');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Cédula de ciudadanía','CC');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Cédula de extranjería','CE');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Pasaporte','PA');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Certificado de nacido vivo','NV');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Carné diplomático','CD');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Salvo conducto de permanencia','SC');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Pasaporte de la ONU','PR');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Permiso Especial de Permanencia','PE');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('NIT','NI');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Adulto sin Identificación','AS');
INSERT INTO sspt_tipo_identificacion (descripcion,tipo) VALUES ('Menor sin Identificación','MS');
INSERT INTO sspt_usuario (apellidos,email,"enable",fecha_registro,identificacion,nombres,"password",username,id_rol,id_tipo_identificacion) VALUES ('ramón montes','omarmontes.879@gmail.com',true,'2019-06-29 15:47:30.000','1093792354','omar','$2a$10$a3syW59aYf6/a1wtf7PYVOFJnTWHe6dlc7mQ91s.u/pN./rLwDn5O','omarrm',1,3);

INSERT INTO sspt_empresa (nit,nombre,razon_social,mision,vision,direccion,telefono,email,enable,fecha_registro) VALUES ('123456','Professional Care','Professional Care es una empresa de centro de pagos y gestión de afiliaciones a seguridad social.','Asesorar y acompañar a propietarios, Socios y empleadores de las diferentes Compañías en el proceso de crecimiento y Estandarización de los procedimientos de Los departamentos (Departamentización) de la empresa. Brindar adecuadas asesorías en asuntos Laborales y de seguridad social, evitando Que nuestros usuarios se vean afectados Por posibles riesgos.','Ser en 2025 la empresa más Importante en administración de Seguridad social y administración Se personal en el municipio de Cúcuta contar con más de 600 Empresas Afiliadas, con un crecImiento progresivo y controlado.','1 N° 11-81 Loca 1 Barrio La Playa','+57 3015406332 - +57 3115407518','profesionalcareseccional@gmail.com',true,now());