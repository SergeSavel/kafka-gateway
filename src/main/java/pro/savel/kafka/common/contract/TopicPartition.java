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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class TopicPartition
{
    private @NotEmpty String topic;
    private @PositiveOrZero int partition;

    private TopicPartition()
    {
    }

    public static Collection<TopicPartition> of(Collection<org.apache.kafka.common.TopicPartition> source)
    {
        if (source == null)
            return null;
        var result = new ArrayList<TopicPartition>(source.size());
        source.forEach(partition -> result.add(of(partition)));
        return result;
    }

    public static TopicPartition of(org.apache.kafka.common.TopicPartition source)
    {
        if (source == null)
            return null;
        var result = new TopicPartition();
        result.topic = source.topic();
        result.partition = source.partition();
        return result;
    }
}
