
# Test - nextdots - Argentina #

---
Se desea implementar una versión simplificada de Airbnb donde un usuario pueda mostrar
un listado (max 30) de los alojamientos disponibles en la ciudad donde se encuentra el
usuario. En caso de que a través del método de geocoding inverso no se pueda obtener la
ciudad, se deben listar los alojamientos disponibles en la ciudad de Los Angeles, CA, USA.
Además de ello, es importante brindarle la posibilidad al usuario de almacenar de forma
local en el dispositivo la información de sus sitios preferidos (favoritos) y acceder a ellos sin
necesidad de conexión a internet.
El listado debe mostrar la imagen, el nombre, el precio por noche y el tipo de alojamiento.
Adicionalmente, el usuario podrá acceder al detalle del alojamiento, el cual contará con la
siguiente información: nombre del alojamiento, precio por noche, tipo de propiedad, tipo de
habitación, dirección pública, cantidad de huéspedes, cantidad de baños, cantidad de
dormitorios, cantidad de camas, descripción del alojamiento y un mini mapa con la ubicación
del alojamiento señalada con un marcador.
El usuario podrá visualizar también en un mapa los sitios de la plataforma de Airbnb
disponibles en su ciudad (max 30). En caso de que a través del método de geocoding
inverso no se pueda obtener la ciudad, se deben mostrar en el mapa los alojamientos
disponibles en la ciudad de Los Angeles, CA, USA.
Los marcadores en el mapa deben incluir el precio por noche por alojamiento.
También es necesario que para poder usar la aplicación el usuario deba ingresar con su
cuenta de Facebook. Una vez hecho esto, se debe almacenar las credenciales en el
dispositivo para iniciar automáticamente la sesión la próxima vez que el usuario ingrese a la
aplicación.

---
### Paquetes de proyecto 

```
contract: Contractos - Model-View-Presenter
data: model y source
model: Modelos de objetos
source: Contracto de repositorio y origenes de datos
di: Inyección de dependencias
presenter: Presetadores
receiver: Capturador de eventos
services: Servicios
ui: activity, adapte, fragment
acvity: Actividades
adapte: Adaptadores
fragment: Fragmentos
util: Utilidades
```

### Librerias

``` 
  circleimageview
  appcompat-v7
  permissiongen
  support-v4:
  design
  dagger
  retrofit2
  gson
  picasso
  logging-interceptor
  cardview-v7
  recyclerview-v7
  butterknife
  facebook-android-sdk
  MlProgress
  ManagerSharedPreferences
  material-dialogs
  play-services-location
  support-annotations
  play-services-maps
  
```
