# Electron

Lightweight practice core base

# Features

- Queues
- Arenas
- Kits
- Kit editor
- Hotbar
- Leaderboards / Elo
- Scoreboard
- Profiles
- Data saving (mongo)
- Tablist (configurable)
- PlaceholderAPI Support
- Matches
- Basic chat features (/msg /reply)

# Setup

I designed this practice core to be as dependencyless as possible which is why it only requires the following:

- Packet events (use the one in the libs folder)
- ProtocolLib
- 1.8
- MongoDB (works without just doesnt save data)

## Compiling

- Clone the repo to your intellij
- Let maven do its magic
- run `mvn package`
- add `target/Practice-1.0.jar` to ur server
- add `libs/packetevents-2.7.0.jar` to ur server
- run ur server for configs to load, add mongo
- and boom... practice server!

# PLEASE NOTE
This is an older version of the core, So expect bugs and what not... but great for a base lol

---
© vifez 2025