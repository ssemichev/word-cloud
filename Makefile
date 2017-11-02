help:
	@echo "test (run tests)"
	@echo "test-it (run IT tests)"
	@echo "test-ete (run E2E tests)"
	@echo "test-bench (run benchmark tests)"
	@echo "test-all (run all tests)"
	@echo "codacy-coverage (update Codacy code coverage)"
	@echo "run-l (run locally)"
	@echo "get-version (get the current version of the project)"
	@echo "docker-stage (publish docker artifacts to ./wordcloud-api-rest/target/docker/ folder)"
	@echo "docker-publish (publish docker image to docker hub)"
	@echo "deploy-p (deploy in production)"

test:
	sbt test

test-it:
	sbt it:test

test-ete:
	sbt ete:test

test-bench:
	sbt bench:test

test-all:
	sbt testAll

codacy-coverage:
	sbt clean coverage testAll
	sbt coverageReport
	sbt coverageAggregate
	export CODACY_PROJECT_TOKEN=$(WORDCLOUD_CODACY_PROJECT_TOKEN); sbt codacyCoverage

run-l:
	sbt "project wordcloud-api-rest" run

get-version:
	sbt "project wordcloud-api-rest" wordcloud-api-rest/version

docker-stage:
	@echo "will publish docker artifacts to ./wordcloud-api-rest/target/docker/ folder"
	sbt "project wordcloud-api-rest" docker:stage

docker-publish:
	sbt "project wordcloud-api-rest" docker:publish

deploy-p:
	sbt testAll
	sbt "project wordcloud-api-rest" "release with-defaults"
