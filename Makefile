SBT         := $(shell command -v sbt 2> /dev/null)

build:
	@sbt reload clean compile

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
