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
public class PartitionInfo
{
    private int partition;
    private Node leader;
    private Collection<Node> replicas;
    private Collection<Integer> isr;
    private Collection<Integer> elr;
    private Collection<Integer> lastKnownElr;

    private PartitionInfo()
    {
    }

    public static Collection<PartitionInfo> of(Collection<org.apache.kafka.common.TopicPartitionInfo> source)
    {
        if (source == null)
            return null;
        var result = new ArrayList<PartitionInfo>(source.size());
        source.forEach(partitionInfoSource -> result.add(of(partitionInfoSource)));
        return result;
    }

    public static PartitionInfo of(org.apache.kafka.common.TopicPartitionInfo source)
    {
        if (source == null)
            return null;
        var result = new PartitionInfo();
        result.partition = source.partition();
        result.leader = Node.of(source.leader());
        result.replicas = Node.of(source.replicas());
        result.isr = mapReplicaIds(source.isr());
        result.elr = mapReplicaIds(source.elr());
        result.lastKnownElr = mapReplicaIds(source.lastKnownElr());
        return result;
    }

    private static Collection<Integer> mapReplicaIds(Collection<org.apache.kafka.common.Node> source)
    {
        if (source == null)
            return null;
        var result = new ArrayList<Integer>(source.size());
        source.forEach(node -> result.add(node == null || node.isEmpty() ? null : node.id()));
        return result;
    }
}
