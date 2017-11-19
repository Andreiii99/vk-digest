# vk-digest

[![Build Status](https://travis-ci.org/atanana/vk-digest.svg?branch=master)](https://travis-ci.org/atanana/vk-digest)

Отсылает по почте сообщения с vk.com

Настройки в config.json:

    {
      "vkConfig": {
        "userId": 123, // идентификатор пользователя 
        "token": "test token", // токен для vk апи 
        "chatId": 321// идентификатор беседы
      },
      "mailConfig": {
        "server": "test server", // адрес почтового сервера
        "port": 456, // порт почтового сервера
        "address": "test@test.com", // отправитель
        "password": "test password", // пароль отправителя
        "addressTo": [ // адреса получателей
              "test_to@test.com",
              "test_to2@test.com"
            ],
        "subjectPrefix": "Дайджест" // префикс письма
      }
    }