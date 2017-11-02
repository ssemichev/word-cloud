[![Build Status]()](https://travis-ci.com/wordcloud/wordcloud)
[![Codacy Badge]()](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=wordcloud/wordcloud&amp;utm_campaign=Badge_Grade)
[![Codacy Badge]()](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=deepwordcloud/wordcloud&amp;utm_campaign=Badge_Coverage)

# wordcloud

![ScreenShot](https://user-images.githubusercontent.com/5940291/32342789-bfc3defe-bfbe-11e7-9a98-d4eda250a921.png)

### Test the service locally

To start dev server

```sh
sbt
wordcloud-api-rest/re-start
```
or
```sh
sbt "project wordcloud-api-rest" run
```

To run test requests

```sh
curl -i -w %{time_connect}:%{time_starttransfer}:%{time_total} http://127.0.0.1:9000/v1/health
curl -i http://127.0.0.1:9000/v1/about
curl -i http://127.0.0.1:9000/v1/status
curl -XGET 'http://127.0.0.1:9000/v1/search?top=100' --user crud-user:crud-password
curl -XPOST 'http://127.0.0.1:9000/v1/url=http://test.com' --user crud-user:crud-password
curl -XGET 'http://127.0.0.1:9000/v1/queuestatus' --user crud-user:crud-password

./simulateRequestsFast.sh http://127.0.0.1 9000/v1 url "--user crud-user:crud-password"
```

### Make
```sh
- test (run tests)
- test-it (run IT tests)
- test-ete (run E2E tests)
- test-bench (run benchmark tests)
- test-all (run all tests)
- codacy-coverage (update Codacy code coverage)
- run-l (run locally)
- get-version (get the current version of the project)
- docker-stage (publish docker artifacts to ./wordcloud-api-rest/target/docker/ folder)
- docker-publish (publish docker image to docker hub)
- deploy-p (deploy in production)
```

### Deployment

To deploy develop branch

```sh
//commit all your changes
git push
```

To deploy master branch

```sh
//commit all your changes
git push
sbt "project wordcloud-api-rest" "release with-defaults"
```

or if your need to set the build version manually
```sh
//commit all your changes
git push
sbt "project wordcloud-api-rest" release
```

To manually create / update  wordcloud-api-rest stack from your local machine

```sh
sbt "project wordcloud-api-rest" docker:publish makeDockerVersion
```

or

```sh
sbt "project wordcloud-api-rest" makeDockerVersion
//modify ./target/docker-image.version file to use existing container
./scripts/deploy/build-wordcloud-api-rest-stack.sh development
```

### Stress tests

```sh
./apache-jmeter-3.0/bin/jmeter -n -t ./wordcloud-api-v1-testplan.jmx --addprop ./user.properties
```

or with SOCKS proxy

```sh
./apache-jmeter-3.0/bin/jmeter -n -t ./wordcloud-api-v1-testplan.jmx --addprop ./user.properties -DsocksProxyHost=localhost -DsocksProxyPort=1234
```

### Show a list of project dependencies that can be updated

```sh
sbt dependencyUpdates
```

### Add test coverage to Codacy locally

```sh
export WORDCLOUD_CODACY_PROJECT_TOKEN=<Project_Token>
```

```sh
export CODACY_PROJECT_TOKEN=WORDCLOUD_CODACY_PROJECT_TOKEN
sbt clean coverage testAll
sbt coverageReport
sbt coverageAggregate
sbt codacyCoverage
```


