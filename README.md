# `SavingsGoals` Coding Test
## Candidate: Fatih Sokmen (fatih.sokmen@gmail.com)

This coding test implements a “round-up” feature using our public developer API that is available to all customers and partners.

App consists of 2 screens
- Home screen loads saved goals if ever exists otherwise shows an action card to create a new one.
- Second screen is for creating a `new saving goal space` by inputting a space bame name, etc `Holiday`

## Architecture
- App is built based on MVVM pattern. ViewModels are state holders and interactions are done over usecases which are independent components to run business logics. Screen states are composite states which are combined by sub-states for different part of screens.
- UI is built by `Compose` which is a modern and powerful toolkit for building native UI. 
- Libraries used: Hilt, Jetpack(compose, navigation), Coroutines/Flow, datastore, kotlinx.date, retrofit and okhttp

## Tools / Env.
- Android Studio Iguana | 2023.2.1 Canary 7 (on Apple M1 Chip)
- Gradle 8.0

## Tests
`ViewModel`s, use cases and repositories are being unit-tested. Some libraries used to test source code

- Junit 4
- Mockk
- Turbine for Flow tests
- Kotest assert for assertions

## Authentication
- App uses sand-box api.
- When no `Bearer` token is used, sandbox backend responds with Http 403 (401 would be expected here). Hence, app can't use a Retrofit `Authenticator` to refresh token, therefore a custom `Interceptor` is need to handle 403 to refresh access token
- Refreshed token is stored in `TokenStore` to be consumed across api calls.

## Improvements considerations

- All repositories use remote api services that some caching can be added to reduce api calls.
  Example: Most api call require `Account` info. `AccountRepository` can cache account info on memory or disk.
- Custom serializer for BigDecimal is needed.




