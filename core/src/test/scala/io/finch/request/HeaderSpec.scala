/*
 * Copyright 2014, by Vladimir Kostyukov and Contributors.
 *
 * This file is a part of a Finch library that may be found at
 *
 *      https://github.com/finagle/finch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor(s):
 * Ryan Plessner
 */

package io.finch.request

import com.twitter.finagle.httpx.Request
import com.twitter.util.Await
import org.scalatest.{Matchers, FlatSpec}

class HeaderSpec extends FlatSpec with Matchers {

  "A RequiredHeader" should "properly read the header field" in {
    val request = Request()
    request.headerMap.update("Location", "some header")
    val futureResult = header("Location")(request)
    Await.result(futureResult) shouldBe "some header"
  }

  it should "error if it does not exist" in {
    val request = Request()
    val futureResult = header("Location")(request)
    a [NotPresent] shouldBe thrownBy(Await.result(futureResult))
  }

  "An OptionalHeader" should "properly read an existing header field" in {
    val request = Request()
    request.headerMap.update("Location", "some header")
    val futureResult = headerOption("Location")(request)
    Await.result(futureResult) shouldBe Some("some header")
  }

  it should "be None if it does not exist" in {
    val request = Request()
    val futureResult = headerOption("Location")(request)
    Await.result(futureResult) shouldBe None
  }

  it should "be None if it's empty" in {
    val request = Request()
    request.headerMap.update("Location", "")
    val futureResult = headerOption("Location")(request)
    Await.result(futureResult) shouldBe None
  }

  "A Header Reader" should "have a matching RequestItem" in {
    val h = "Location"
    header(h).item shouldBe items.HeaderItem(h)
    headerOption(h).item shouldBe items.HeaderItem(h)
  }
}
