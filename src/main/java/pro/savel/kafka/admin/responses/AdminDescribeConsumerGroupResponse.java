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

package pro.savel.kafka.admin.responses;

import lombok.Getter;
import pro.savel.kafka.admin.AdminResponseMapper;
import pro.savel.kafka.common.contract.Node;

import java.util.Collection;

@Getter
public class AdminDescribeConsumerGroupResponse implements AdminResponse
{
    private String groupId;
    private boolean isSimpleConsumerGroup;
    private Collection<GroupMemberDescription> members;
    private String partitionAssignor;
    private String type;
    private String groupState;
    private Node coordinator;
    private Collection<String> authorizedOperations;
    private Integer groupEpoch;
    private Integer targetAssignmentEpoch;

    private AdminDescribeConsumerGroupResponse()
    {
    }

    public static AdminDescribeConsumerGroupResponse of(org.apache.kafka.clients.admin.ConsumerGroupDescription source)
    {
        if (source == null)
            return null;
        var result = new AdminDescribeConsumerGroupResponse();
        result.groupId = source.groupId();
        result.isSimpleConsumerGroup = source.isSimpleConsumerGroup();
        result.members = GroupMemberDescription.of(source.members());
        result.partitionAssignor = source.partitionAssignor();
        result.type = AdminResponseMapper.mapGroupType(source.type());
        result.groupState = AdminResponseMapper.mapGroupState(source.groupState());
        result.coordinator = Node.of(source.coordinator());
        result.authorizedOperations = AdminResponseMapper.mapAclOperations(source.authorizedOperations());
        result.groupEpoch = source.groupEpoch().orElse(null);
        result.targetAssignmentEpoch = source.targetAssignmentEpoch().orElse(null);
        return result;
    }
}
