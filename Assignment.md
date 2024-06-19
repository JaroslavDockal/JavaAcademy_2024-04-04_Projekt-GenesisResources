# Projekt registračního systému Genesis Resources

## Přehled

V postapokalyptickém světě potřebuje organizace Genesis Resources robustní registrační systém pro spravedlivou distribuci životně důležitých surovin z Oázy.

## Požadavky projektu

- **REST API**
- **Ukládání dat do databáze**
- **Správné rozdělení kódu (controller, service, atd.)**
- **Čistota kódu**
- **Export z Postmana pro testování API**
- **SQL příkazy pro generování databáze**

## Požadované operace API

### 1. Založení nového uživatele
- **Endpoint**: `POST /api/v1/user`
- **Tělo požadavku**:
    ```json
    {
      "name": "string",
      "surname": "string",
      "personID": "string(12)"
    }
    ```
- **Poznámka**: `personID` musí být jedinečné.

### 2. Informace o uživateli
- **Endpoint**: `GET /api/v1/user/{ID}`
- **Odpověď**:
    ```json
    {
      "id": "string",
      "name": "string",
      "surname": "string"
    }
    ```
- **Detailní varianta**: `GET /api/v1/user/{ID}?detail=true`
    ```json
    {
      "id": "string",
      "name": "string",
      "surname": "string",
      "personID": "string",
      "uuid": "string"
    }
    ```

### 3. Informace o všech uživatelích
- **Endpoint**: `GET /api/v1/users`
- **Odpověď**:
    ```json
    [
      {
        "id": "string",
        "name": "string",
        "surname": "string"
      }
    ]
    ```
- **Detailní varianta**: `GET /api/v1/users?detail=true`
    ```json
    [
      {
        "id": "string",
        "name": "string",
        "surname": "string",
        "personID": "string",
        "uuid": "string"
      }
    ]
    ```

### 4. Upravit informace o uživateli
- **Endpoint**: `PUT /api/v1/user`
- **Tělo požadavku**:
    ```json
    {
      "id": "string",
      "name": "string",
      "surname": "string"
    }
    ```

### 5. Smazat uživatele
- **Endpoint**: `DELETE /api/v1/user/{ID}`

## Požadavky na databázi

### Tabulka: Users

| Sloupec   | Typ     | Omezení                                         |
|-----------|---------|-------------------------------------------------|
| ID        | Long    | PrimaryKey, Unique, NotNull, Autoincrement      |
| Name      | Varchar | NotNull                                         |
| Surname   | Varchar |                                                 |
| PersonID  | Varchar | Unique, NotNull                                 |
| UUID      | Varchar | Unique, NotNull                                 |


### Generování UUID
- Použijte UUID knihovnu z [Baeldung](https://www.baeldung.com/java-uuid).

## Doporučená vylepšení
- Logovací framework
- Front-end implementace
- JUnit testy
- Vytvoření dvou profilů

