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

package pro.savel.kafka.common;

import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CommonRequestMapper
{
    public static Set<TopicPartition> mapPartitions(Collection<pro.savel.kafka.common.contract.TopicPartition> source)
    {
        if (source == null)
            return null;
        var result = new HashSet<TopicPartition>(source.size());
        source.forEach(topicPartition -> result.add(mapTopicPartition(topicPartition)));
        return result;
    }

    public static TopicPartition mapTopicPartition(pro.savel.kafka.common.contract.TopicPartition source)
    {
        if (source == null)
            return null;
        return new TopicPartition(source.getTopic(), source.getPartition());
    }
}
