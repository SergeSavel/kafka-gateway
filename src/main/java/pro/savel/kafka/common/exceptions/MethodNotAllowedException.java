// Copyright 2025 Sergey Savelev (serge@savel.pro)
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

package pro.savel.kafka.common.exceptions;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

public class MethodNotAllowedException extends HttpStatusException
{
    private final HttpMethod allowedMethod;

    public MethodNotAllowedException(String message, HttpMethod allowedMethod, Throwable cause)
    {
        super(message, cause);
        this.allowedMethod = allowedMethod;
    }

    public MethodNotAllowedException(String message, HttpMethod allowedMethod)
    {
        super(message);
        this.allowedMethod = allowedMethod;
    }

    public HttpMethod allowedMethod()
    {
        return allowedMethod;
    }

    @Override
    public HttpResponseStatus status()
    {
        return HttpResponseStatus.METHOD_NOT_ALLOWED;
    }
}
