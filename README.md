# Wallet Service Coding Challenge 💰

This is a Java 17 Spring Boot 3 project implemented using hexagonal architecture that includes
features such as
records, streams API, Specifications, Paginable, JpaRepository, ControllerAdvice,
RestControllers, and enums among others. All whilst
adhering to [Google's Java Style Guide](https://google.github.io/styleguide/javaguide.html) you too
can
get a format spec for your favourite IDE [here](https://github.com/google/styleguide) 😁

## Technologies Used

- Docker 🐳
- Java 17 ☕️
- Spring Boot 3 🍃
- PostgreSQL 🐘
- Mockito 5 & JUnit 5 🧪

## Architecture

This project follows a hexagonal architecture pattern, with ports and adapters. JpaRepositories
create their adapter at runtime so there's no need for the output adapter folder. Communication
between different components is done through interfaces and DTOs

## Running the Application with Docker Compose

To run the application using Docker Compose, follow these steps:

1. Clone the repository to your local machine
2. Navigate to the project directory
3. Run the following command to start the application and its database ```docker-compose up```
4. The application should now be running on `http://localhost:8001`
5. PostgreSQL should be running on port `5432` with user **postgres**, password **postgres** and
   database called `wallet_service_db`
6. Starter data is automatically seeded and reset with each run
7. Should you run into any issues, it can be built with maven and it runs on the same ports locally,
   you'd just have to ensure that the `wallet_service_db` exists before running it so that hibernate
   can populate it

## Dockerized Components

The entire application (Spring App & PostgreSQL) is dockerized, the maven build process also happens
within the container to ensure portability.

## Solution considerations and implementations

* This project provides an implementation of Wallet, BankAccounts, Transactions and Payments
  the implementation is served through 4 REST API endpoints:

    * Check wallet balance
    * Set bank account details to transfer funds to and from
    * Create wallet transactions
    * Retrieve wallet transaction history

* Every **User** has a **Wallet**
* All Wallets are linked to OnTop's **BankAccount**
* **Wallet** and **BankAccount** funds are checked before each withdraw
* Creating a **WalletTransaction** triggers an interaction with the **Payment** provider, this
  interaction generates both
  a **WalletTransaction** and **PaymentTransaction** and moves the funds
* All fund transfers happen from **BankAccount** to **BankAccount** i.e. your Wallet (**OnTop's
  BankAccount** & your other **BankAccount**)
* A 10% fee is applied to all **Wallet withdrawals** and credited to the OnTop Bank Account. i.e if
  transferring $1000.00 the destination BankAccount receives $900.00 with the remaining $100.00
  going to the OnTop BankAccount
* **Wallet** balances are synchronized to reflect **PaymentTransactions** and status
* **Refunds** are honored on failed withdrawals and a new transaction is created to move the money
  back

    * Some exceptions like throwing 500 and 404 on matching user_id where thrown for fun since
      they were included in the mocks 😁

## Process flow

The Postman collection is included inside the `resources` folder. You can access the
folder [here](src/main/resources).

* You are provided with user_ids 1 & 2 starting with **$10000.00** and **$5000.00** respectively in
  their
  wallets
* You can check your funds with your user_id at any time by
  visiting `localhost:8001/wallets/balance?user_id=1`
* Use the endpoint `localhost:8001/wallets/transactions/bank` to set your bank account details
* Once the banking details are set you are free to move money to and from your user's
  wallet `localhost:8001/wallets/transactions` until you run out of funds, just click away until
  your run out of funds, then transfer some more and keep going.
* You can also view your transaction history at any point
  through ```localhost:8001/wallets/transactions/history``` this api provides granularity by
  providing date range and amount range filters. Results are ordered in descending order and the
  amount of results per page as well as the page can be adjusted through the request.
* Fun-fact all dummy data in the database are a reference to The Little Prince 👑🦊🌹

