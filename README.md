### **База данных filmorate**<br>  

![База данных filmorate](src/main/resources/images/filmorate.png)

### **Таблица users**<br>
Содержит информацию о пользователях   
Таблица включает такие поля:
* первичный ключ **user_id** — идентификатор пользователя;  
* **email** - электронная почта пользователя;  
* **login** - логин пользователя;  
* **name** - имя пользователя;  
* **birthday** - день рождения пользователя.    

### **Таблица friends**<br>
Содержит информацию о друзьях пользователей  
Таблица включает такие поля:
* внешний ключ **user_id** (ссылается на таблицу **users**) - идентификатор пользователя;  
* внешний ключ **friend_id** (ссылается на таблицу **users**) - идентификатор друга пользователя;  
* **confirmation** - подтверждение дружбы.    


### **Таблица films**<br>
Содержит информацию о фильмах  
Таблица включает такие поля:
* первичный ключ **film_id** — идентификатор фильма;  
* **name** - название фильма;  
* **description** - описание фильма;  
* **realiseDate** - дата выхода фильма;  
* **duration** - продолжительность фильма в минутах;  
* внешний ключ **rating** (ссылается на таблицу **rating**) - рейтинг Ассоциации кинокомпаний. Эта оценка определяет возрастное ограничение для фильма.    

### **Таблица rating**<br>
Содержит информацию об оценках, определяющих возрастное ограничение для фильма.  
Таблица включает такие поля:
* первичный ключ **rating_id** - идентификатор рейтинга;  
* **rating-name** - название рейтинга.    

### **Таблица genre**<br>
Содержит информацию о жанрах  
Таблица включает такие поля:
* первичный ключ **genre_id** - идентификатор рейтинга;  
* **name** - название жанра.    

### **Таблица film_genre**<br>
Содержит информацию о фильмах и их жанрах  
Таблица включает такие поля:
* внешний ключ **film_id** (ссылается на таблицу **films**) - идентификатор фильма;  
* внешний ключ **genre_id** (ссылается на таблицу **genre**) - идентификатор жанра.    

### **Таблица likes**<br>
Содержит информацию о лайках  
Таблица включает такие поля:
* внешний ключ **film_id** (ссылается на таблицу **films**) - идентификатор фильма;  
* внешний ключ **user_id** (ссылается на таблицу **users**) - идентификатор пользователя.


### Примеры запросов
#### Вывод количества общих друзей пользователей с id 1 и id 2:  

```
SELECT COUNT(f1.friend_id)
FROM friends AS f1
JOIN friends AS f2 ON f1.friend_id = f2.friend_id
WHERE f1.user_id = 1 AND f2.user_id = 2;
```


#### Вывод 10 самых популярных фильмов  

```
SELECT films.name,
    COUNT(films.film_id)
FROM likes
JOIN films ON likes.film_id = films.film_id
GROUP BY likes.film_id
ORDER BY COUNT(likes.film_id) DESC
LIMIT 10;
```















