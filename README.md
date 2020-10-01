# Aplikacja pobierająca i udostępniająca dane postów

Aplikacja co 24 godziny pobiera ze strony https://jsonplaceholder.typicode.com/posts listę postów.
Po raz pierwszy zostaje ona pobrana 5 sekund po uruchomieniu aplikacji.

## Prerekwizyty

* Java 11

* Baza danych PostgreSQL na domyślnym porcie (5432), zgodna z wartościami z pliku application.properties:

    * Nazwa: postsdb

    * Użytkownik: posts

    * Hasło: test


## Jak zacząć

Aby szybko rozpocząć korzystanie z aplikacji można pobrać plik posts.jar z folderu src/main/target i wykonać polecenie:

```
java -jar posts.jar
```

Aplikacja uruchomi się pod adresem https://localhost:8080/ .

## Instalacja

Po sklonowaniu [repozytorium](https://github.com/mduczmal/posts) aplikację można uruchomić poleceniem:
```
./mvnw spring-boot:run
```
Testy można uruchomić używając polecenia:
```
./mvnw test
```


## API

Odpowiedzi na requesty GET są w zgodne ze standardem HAL.

### Pobieranie postów
Listę wszystkich pobranych przez aplikację post postów można pobrać wykonując request GET pod adresem http://localhost:8080/posts .
```
curl http://localhost:8080/posts
```

Pojedynczy post można pobrać wykonując request GET pod adresem http://localhost:8080/posts/<id\> .

Na przykład, aby pobrać post o polu id 1 należy wykonać polecenie:
```
curl http://localhost:8080/posts/1
```

Listę wszystkich postów o tytułach zawierających jako podciąg znaków <wyszukiwana_fraza> można pobrać wykonując request GET pod adresem
http://localhost:8080/posts?search=<wyszukiwana_fraza>

Na przykład, aby pobrać listę postów, które w tytule zawierają słowo "rekrutacja" należy wykonać polecenie:
```
curl http://localhost:8080/posts?search=rekrutacja
```

### Aktualizacja listy postów na żądanie
Aby nakazać aplikacji aktualizację postów, należy wykonać request POST pod adresem http://localhost:8080/posts .
```
curl -X POST http://localhost:8080/posts
```

### Usuwanie postów
Post można usunąć wykonując request DELETE pod adresem http://localhost:8080/posts/<id\>, gdzie zamiast \<id\> należy podać id posta.

Na przykład, aby usunąć post o polu id 1 należy wykonać polecenie:
```
curl -X DELETE http://localhost:8080/posts/1
```

### Modyfikacja postów
Tytuł posta można zmodyfikować wykonując request PUT pod adresem http://localhost:8080/posts/<id\>?title=<nowy_tytuł>, 
gdzie zamiast \<id\> należy podać id posta, a zamiast <nowy_tytuł> zmodyfikowany tytuł posta.

Pole body posta można zmodyfikować wykonując request PUT pod adresem http://localhost:8080/posts/<id\>?body=<nowe_pole_body>, 
gdzie zamiast \<id\> należy podać id posta, a zamiast <nowe_pole_body> zmodyfikowane pole body posta.

Można w jednym requeście PUT zmodyfikować równocześnie tytuł i pole body:
http://localhost:8080/posts/<id\>?title=<nowy_tytuł>&body=<nowe_pole_body>

Na przykład, aby zmodyfikować tytuł posta o id 1 na "nowy", a pole body na "nowe" należy wykonać polecenie:
```
curl -X PUT "http://localhost:8080/posts/1?title=nowy&body=nowe"
```

## Autor

**Mikołaj Duczmal**

