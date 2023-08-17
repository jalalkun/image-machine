# Image Machine

This app for save image by name and type



## Tech Stack

**UI:** [Material 3](https://m3.material.io/develop/android/jetpack-compose)

**DB:** [ROOM](https://developer.android.com/jetpack/androidx/releases/room?hl=id)

**Image Processing:** [Coil](https://coil-kt.github.io/coil/)

**Dependency Injection:** [Koin](https://insert-koin.io/)

**QR Scanner:** [Mlkit Barcode Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning/android?hl=id)




## Demo

In-Home Screen, the app has two buttons. 
Machine Data to List Machine Data Screen. 
Code Reader to read QR and direct to Machine Data detail

![home](https://github.com/jalalkun/image-machine/blob/main/home.jpg)

List Machine Data Screen.
This screen has a data list, filter sort by, and button Add MachineData.

![List Data Screen](https://github.com/jalalkun/image-machine/blob/main/list.jpg)
![List Sort](https://github.com/jalalkun/image-machine/blob/main/list_sort.jpg)

Add Machine Data Screen
This screen has fields to enter detail.
Button to add images.
Button to save data.

![Add Machine Data](https://github.com/jalalkun/image-machine/blob/main/add.jpg)


Detail Machine Data Screen
This screen has fields for detail.
and images in below detail section.

This screen has two modes, read and edit.

in the read mode,  just have the feature delete the whole data by using the button delete. 

In edit mode, can edit the detail and the images. 
can select multiple images to delete.
after selecting images to delete click the button Delete Image, but the image data not remove from the database.
need to save the data to save the detail and remove image data from the database, just click the button save.

![Detail](https://github.com/jalalkun/image-machine/blob/main/detail.jpg)
![Detail Edit](https://github.com/jalalkun/image-machine/blob/main/detail_edit.jpg)
