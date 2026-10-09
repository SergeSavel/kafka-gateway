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

package pro.savel.kafka.common.contract;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class Node
{
    private int id;
    private String host;
    private int port;
    private String rack;
    private boolean isFenced;

    private Node()
    {
    }

    public static Collection<Node> of(Collection<org.apache.kafka.common.Node> source)
    {
        if (source == null)
            return null;
        var result = new ArrayList<Node>(source.size());
        source.forEach(nodeSource -> result.add(of(nodeSource)));
        return result;
    }

    public static Node of(org.apache.kafka.common.Node source)
    {
        if (source == null || source.isEmpty())
            return null;
        var result = new Node();
        result.id = source.id();
        result.host = source.host();
        result.port = source.port();
        result.rack = source.rack();
        result.isFenced = source.isFenced();
        return result;
    }
}
