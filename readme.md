# cljs vs phaser
Repo with the minimal boilerplate to develop a [phaser](https://phaser.io) game using [cljs](https://clojurescript.org/)

This is an rewrite of [Body Push Blocks](https://phaser.io/examples/v3.85.0/physics/arcade/view/body-push-blocks) example.

## Requirements
- [npm](https://www.npmjs.com/)
- [clojure](https://clojure.org/)

## Instructions

### Bootstrap
```bash
npm install
```

### Watch on http://localhost:5000
```bash
npm run watch
```

### Release Build (release/public)
```bash
npm run release
```

#### Serve Release bundle
```bash
gzip resources/public/js/*.js
npx http-server resources/public -g
```
