package wordcloud.wordcloudapi.rest.common

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.server.{ Directives, Route }
import wordcloud.common.traits.{ Contract, DomainObject, Translator }
import wordcloud.wordcloudapi.rest.common.json.Json4sJacksonSupport

trait BaseServiceRoute extends Directives with ResponseHandling with Json4sJacksonSupport

trait ResponseHandling {
  self: Directives with Json4sJacksonSupport =>

  def respond(code: StatusCode): Route = {
    complete(code)
  }

  def respond[From <: DomainObject, To <: Contract](value: From)(implicit translator: Translator[From, To]): Route = {
    complete(translator.translate(value))
  }

  def respond[From <: DomainObject, To <: Contract](code: StatusCode, value: From)(implicit translator: Translator[From, To]): Route = {
    complete((code, translator.translate(value)))
  }

  def respond[From <: DomainObject, To <: Contract](values: Seq[From])(implicit translator: Translator[From, To]): Route = {
    complete(values map translator.translate)
  }
}