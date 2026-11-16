# Real Estate Brokerage System (Java · OOP · Design Patterns)

A Java system that models a real-estate brokerage office: sellers, buyers and realtors, properties
(including internal apartments inside a main apartment), add-on services, purchases and property search.
Properties are loaded from JSON with Gson. Built for the Object-Oriented Programming course at Ariel University,
with an emphasis on design patterns and unit testing.

## Design patterns

| Pattern | Where it is used |
|---|---|
| **Singleton** | `PropertyManager.getInstance()`, `UserService.getInstance()` |
| **Factory** | `PropertyFactory`, `PropertyCollectionFactory`, `FileReaderFactory` |
| **Strategy** | pluggable storage (`PropertyCollectionStrategy` → `HashMapPropertyCollectionStrategy`) and file loading (`FileReaderStrategy` → `JsonFileReaderStrategy`) |
| **Adapter** | `PropertyTypeAdapter` – Gson `TypeAdapter` that maps JSON to the property hierarchy |
| **Decorator** | `PropertyWithServices` adds services (cleaning, design, transport, contract guarantor) to a property |

## Features
- Add properties and internal apartments, with duplicate-address checks
- Buy properties through a realtor, track owned and sold properties
- Query unsold properties and property details
- Load properties and owners from a JSON file
- JUnit 5 tests for buyers, sellers, realtors, factories, the manager, the adapter and the JSON reader

## Running
Open the folder in IntelliJ IDEA, add **Gson** and **JUnit 5** to the project libraries, and run `Main` or the tests.
Sample data is in `data/properties.json`.

## Tech
Java 14+ · Gson · JUnit 5 · IntelliJ IDEA
