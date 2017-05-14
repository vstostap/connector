SBT         := $(shell command -v sbt 2> /dev/null)

build:
	@sbt reload clean compile

build-node:
	@rm -rf ./src/main/resources/web/node_modules && npm i ./src/main/resources/web --prefix ./src/main/resources/web

build-webpack:
	@rm -rf ./src/main/resources/web/build && npm run build --prefix ./src/main/resources/web

serve:
	@sbt run

package:
	@sbt package

node:
	@node ./src/main/resources/web/node/index.js

test:
	@sbt test

lint:
	true
