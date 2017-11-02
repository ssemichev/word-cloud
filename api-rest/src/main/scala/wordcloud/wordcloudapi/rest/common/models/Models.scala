package wordcloud.wordcloudapi.rest.common.models

final case class About(serviceName: String, currentUtc: String, version: String, builtAt: String)

final case class Status(serviceName: String, uptime: String)

final case class HealthCheckResponse(ok: Boolean)

