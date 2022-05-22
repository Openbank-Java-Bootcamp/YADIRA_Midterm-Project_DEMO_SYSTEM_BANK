# SYSTEM BANK DEMO

by Yadira Calzadilla


## Description of the Project
The project consists in a banking system demo. Includes a Java/Spring Boot backend, storage in MySQL database tables, GET, POST, PUT/PATCH, and DELETE routes, authentication with Spring Security and error handling.
For a better understanding of the project, the following class diagram is shown below. It is divided into classes from which repositories and database tables are derived. In this case is present the use of single table inheritance for some classes that extends from an abstract class. There are other classes that support the structure: enums and embeddables.

![class diagram (3)](https://user-images.githubusercontent.com/100872227/169347218-0612a306-b937-4dfc-808c-356f76037433.jpg)

# Data Base Model

As result of the database structure construction there are six tables: 
-> role. Contains the possible roles for a user, in this case: “ROLE_ADMIND” and “ROLE_ACCOUNT_HOLDER”.
-> user. Contains all information about admins and account holders.
-> account. Storage three types of accounts: checking, student_checking and saving.
-> credit_card
-> third_party. Has essential data from extern users of the bank.
-> transfer. Store all the transaction between an account and another account and an account and a third_party.

![DATABASE-MODEL](https://user-images.githubusercontent.com/100872227/169347354-a514a0bd-68db-4604-a3fe-d46bc2966efe.jpg)


# Controller and Service Classes

To complete the requirements, the project consists essentially of three controller classes which use service classes to make their functionalities possible.

![Controller Diagram (2)](https://user-images.githubusercontent.com/100872227/169347693-57262b22-ec5a-47e2-b35a-a7ed6bebeb8b.jpg)


## USER CONTROLLER

The UserController class allows the admin to create an account_holder through the POST method and the endpoint http://localhost:8080/api/account_holders.


## ACCOUNT CONTROLLER

The AccountController also allows the admin to create every type of accounts through the POST method. The account is assigned to existing users(account_holders) in the database.

For creating a saving account the endpoint is http://localhost:8080/api/accounts/savings.

For a credit card the endpoint is http://localhost:8080/api/accounts/credit_cards.

For checking and student_checking the endpoint is http://localhost:8080/api/accounts/checkings. In that case, is the primary owner is less or more than 24, the system automatically assigns the type of account.

For a third_party the endpoint is http://localhost:8080/api/accounts/third_parties. In this case, only a name is passed as a parameter, and the hashedKey is automatically generated for each ThirdParty.

With the AccounController also each account_holder, through the GET method, once authenticated, could check their balance account and, according to the date of the last consultation made (lastConsultation in the account table in the database), interest or pending charges are added or subtracted.

For consulting an account balance, is necessary the id of the accountand the endpoint is http://localhost:8080/api/accounts/{id}.

The same with the credit_card, in this case: http://localhost:8080/api/accounts/credit_card/{id}.

With this controller class the admin, through the PATCH method could decrease the balance of any account in the endpoint http://localhost:8080/api/accounts/ {id}. And can delete an account with the DELETE method in the same endpoint.


## TRANSFER CONTROLLER

The last controller class is TransferController. In this case, through the POST method account holders can send money to another account or to a third party with http://localhost:8080/api/transfer/account-account/ {id} or http://localhost:8080/api/transfer/account-third_party/ {id} 

Also a third_party can send money to an account in http://localhost:8080/api/transfer/ third_party- account/ {id}.

Every time is generated a new transfer and the balance of each account is modified. In each transfer, it is verified that the receiving account has not received more than two transactions in the last second. It is also verified that the amount of the transfer is not greater than 150% of the largest transfer made to that account in a 24-hour period. In either case, possible fraud is detected and the account is FROZEN. Being frozen you will not be able to send or receive money, only if an administrator changes its status back to ACTIVE. This is posible in http://localhost:8080/api/accounts/status/ {id} with method PATCH.

