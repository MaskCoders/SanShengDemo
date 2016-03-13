#java -jar signapk.jar platform.x509.pem platform.pk8 RealarmApp.apk RealarmApp_signed.apk
java -jar signapk.jar platform.x509.pem platform.pk8 $1 $1_signed
