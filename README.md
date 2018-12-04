## Registro
* Ese endpoint deberá recibir un usuario con los campos "nombre", "correo", "contraseña", más un listado de objetos "teléfono", respetando el siguiente formato:
```json
    {
        "name": "Juan Rodriguez",
        "email": "juan@rodriguez.org",
        "password": "hunter2",
        "phones": [
            {
                "number": "1234567",
                "citycode": "1",			
                "contrycode": "57"
            }
        ]
    }
```
* Responder el código de status HTTP adecuado
* En caso de éxito, retorne el usuario y los siguientes campos:
   * `id`: id del usuario (puede ser lo que se genera por el banco de datos, pero sería más deseable un UUID)
   * `created`: fecha de creación del usuario
   * `modified`: fecha de la última actualización de usuario
   * `last_login`: del último ingreso (en caso de nuevo usuario, va a coincidir con la fecha de creación)
   * `token`: token de acceso de la API (puede ser UUID o JWT)
* Si caso el correo conste en la base de datos, deberá retornar un error "El correo ya registrado".
* El token deberá ser persistido junto con el usuario
## Login
* Este endpoint va a recibir un objeto con correo y contraseña.
* Si caso el correo y contraseña correspondan a un usuario registrado, retornar tal cual al endpoint de creación.
* En caso del correo no estar registrado, debe retornar un error con status adecuado y el mensaje "Usuario o contraseña inválidos"
* En caso del correo estar registrado correctamente y la contraseña no ser correspondiente a él, retornar o status adecuado 401 con el mensaje " Usuario o contraseña inválidos "
## Perfil del Usuario
* Caso no se encuentre el token, retornar error con status adecuado y el mensaje "No autorizado".
* Si caso haya el token, buscar el usuario por el id enviado en el path y comparar si el token en el modelo es igual al token lo que es presentado en el header.
* En caso de que los tokens no coincidan, retornar error con status adecuado y el mensaje "No autorizado"
* Caso sea el mismo token, verificar si el último login ha sido hace MENOS que 30 minutos. Si caso no haga accedido hace MENOS que 30 minutos, retornar error con el status adecuado y el mensaje "Sesión inválida".
* Caso todo esté correcto, retornar el usuario en el mismo formato de la respuesta de Login.

## Datos de prueba
* Registro:
    - URL: https://desafio-java-wsolano.herokuapp.com/registro
    - BODY: 
        ```json
        {
            "name": "Wladimir Solano",
            "email": "wsolano@wsolano.cl",
            "password": "hunter1",
            "phones": [
                {
                    "number": "1234567",
                    "citycode": "1",			
                    "contrycode": "57"
                },
                {
                    "number": "7654321",
                    "citycode": "1",			
                    "contrycode": "58"
                }
            ]
        }
        ```
    - HEADER: Content-Type:application/json
    
* Login
    - URL: https://desafio-java-wsolano.herokuapp.com/login
    - BODY: 
        ```json
        {
            "email": "wsolano@wsolano.cl",
            "password": "hunter1"
        }
        ```
    - HEADER: Content-Type:application/json
    
* Perfil Usuario
    - URL: https://desafio-java-wsolano.herokuapp.com/id => sustituir por el id del usuario a consultar
    - HEADER: token = tokenUsuario => Corresponde al token creado al momento del registro