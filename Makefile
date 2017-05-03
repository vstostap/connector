SBT         := $(shell command -v sbt 2> /dev/null)


build:
	@sbt reload clean compile

serve:
	@sbt run

package:
	@sbt package

test:
	@sbt test

lint:
	true