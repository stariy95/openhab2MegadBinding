# OpenHAB 2 MegaD binding
режимы работы: "in", "out", "dimmer", "temp", "humidity", "onewire", "adc", "at", "st", "ib", "tget", "contact", в процессе "i2c".

## как запустить? 

### 1) через PaperUI

Configuration > System > Item Linking

simple mode is turned off

Save


Inbox -> MegaD Binding -> Choose Thing

Bridge Megad incoming server adapter

OK

Inbox -> MegaD Binding -> Choose Thing

MegaD Binding Thing

Bridge Selection - > Bridge Megad incoming server adapter - megad:bridge:megadeviceincoming

Configuration Parameters

OK

Configuration > Things

MegaD Binding Thing

Channels

link

### 2) через файлы

.things:

```
Bridge megad:bridge:megadeviceincoming
{
 Thing device onewire [hostname="localhost", port="3", password="sec", refresh="10"]
 Thing device kitchenout [hostname="localhost", port="1", refresh="0"]
 Thing device bedroomcontact [hostname="localhost", port="2", refresh="0"]
}
```


.items:
```
Number Temperature_GF_Corridor  "Temperature [%.1f °C]" <temperature>   (Temperature, GF_Corridor) { channel = "megad:device:megadeviceincoming:onewire:onewire" }
Switch MegaDBindingThing_Input  "Temperature " (Temperature, GF_Corridor) { channel = "megad:device:megadeviceincoming:kitchenout:out" }  
Contact MegaDContact  "[%s]" (Temperature, GF_Corridor) { channel = "megad:device:megadeviceincoming:bedroomcontact:contact" }
```


Принцип такой: 
#### 1) Создаем бридж в файле .things.

```
Bridge megad:bridge:megadeviceincoming {}
```

megad:bridge: - обязятельное поле, после двоеточия - произвольное название.

#### 2) Добавляем Thing (По сути наши порты для меги) внутрь фигурных скобок
```
Bridge megad:bridge:megadeviceincoming
{
Thing device onewire [hostname="localhost", port="3", password="sec", refresh="10"]
Thing device kitchenout [hostname="localhost", port="1", refresh="0"]
Thing device bedroomcontact [hostname="localhost", port="2", refresh="0"]
}
```
device - обязательное поле, далее произвольное название

#### 3) открываем .items и создаем наши переменные.
```
Number Temperature_GF_Corridor "Temperature [%.1f °C]" <temperature> (Temperature, GF_Corridor) { channel = "megad:device:megadeviceincoming:onewire:onewire" }
Switch MegaDBindingThing_Input "Temperature " (Temperature, GF_Corridor) { channel = "megad:device:megadeviceincoming:kitchenout:out" } 
Contact MegaDContact "[%s]" (Temperature, GF_Corridor) { channel = "megad:device:megadeviceincoming:bedroomcontact:contact" }
```
Последний параметр - режимы работы(каналы). до этого - путь, который мы создали в .things (megad:device:megadeviceincoming: - это название бриджа, bedroomcontact: - название Thing )


#### 4) Далее аналогично 1 версии опенхаба

## Как собрать?
1. Скачать: 
	[Java](https://jdk.java.net/12/)
	[Maven](https://maven.apache.org/download.cgi)

2. Загрузить архив со всеми плагинами [отсюда](https://github.com/openhab/openhab2-addons/archive/master.zip) и распаковать
3. Загрузить архив Мегад [отсюда](https://github.com/Pshatsillo/openhab2MegadBinding/archive/master.zip) и распаковать
2. Скопировать директорию `org.openhab.binding.megad` в папку `/openhab2-addons/bundles`.
3. Перейти в скопированную папку и выполнить `mvn clean install`. Сборка должна пройти успешно и в папке `target` появиться архив с байндингом:
```bash
org.openhab.binding.megad git:(master) ✗ ls -l target | grep megad
-rw-r--r--   1 xxxxxxx  staff  29482 10 мар 21:35 org.openhab.binding.megad-2.5.0-SNAPSHOT.jar
```
## Или скачать готовый jar файл [отсюда](https://github.com/Pshatsillo/openhab2MegadBinding/releases)

## Еще

По многочисленным просьбам - Donate:

[Paypal](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=P38VCVDQMSMYQ) 

[Yandex.Money](https://money.yandex.ru/to/410011024847033)

Спасибо!
