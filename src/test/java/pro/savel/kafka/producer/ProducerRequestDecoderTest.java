// Copyright 2026 Sergey Savelev (serge@savel.pro)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package pro.savel.kafka.producer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import jakarta.validation.Validation;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pro.savel.kafka.common.RequestBearer;
import pro.savel.kafka.producer.requests.ProducerCreateRequest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProducerRequestDecoderTest {

    private EmbeddedChannel channel;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        var validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        channel = new EmbeddedChannel(new ProducerRequestDecoder(objectMapper, validatorFactory));
    }

    @AfterEach
    void tearDown() {
        channel.finishAndReleaseAll();
    }

    @Test
    void decodeCreate_blankName_returnsBadRequest() {
        assertFalse(channel.writeInbound(createRequest("   ")));

        FullHttpResponse response = channel.readOutbound();
        assertEquals(HttpResponseStatus.BAD_REQUEST, response.status());
        assertTrue(response.content().toString(StandardCharsets.UTF_8).startsWith("name:"));
        response.release();
    }

    @Test
    void decodeCreate_nonBlankName_passesRequest() {
        assertTrue(channel.writeInbound(createRequest("orders-producer")));

        RequestBearer bearer = channel.readInbound();
        assertEquals("orders-producer", ((ProducerCreateRequest) bearer.request()).getName());
    }

    private static FullHttpRequest createRequest(String name) {
        var body = Unpooled.copiedBuffer("{\"name\":\"" + name + "\",\"config\":{\"bootstrap.servers\":\"kafka:9092\"},"
                + "\"expirationTimeout\":60000}", StandardCharsets.UTF_8);
        var request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/producer/create", body);
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());
        return request;
    }
}
