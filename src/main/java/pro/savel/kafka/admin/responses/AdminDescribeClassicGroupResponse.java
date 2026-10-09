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
public class AdminDescribeClassicGroupResponse implements AdminResponse
{
    private String groupId;
    private String protocol;
    private String protocolData;
    private Collection<GroupMemberDescription> members;
    private String state;
    private Node coordinator;
    private boolean isSimpleConsumerGroup;
    private Collection<String> authorizedOperations;

    private AdminDescribeClassicGroupResponse()
    {
    }

    public static AdminDescribeClassicGroupResponse of(org.apache.kafka.clients.admin.ClassicGroupDescription source)
    {
        if (source == null)
            return null;
        var result = new AdminDescribeClassicGroupResponse();
        result.groupId = source.groupId();
        result.protocol = source.protocol();
        result.protocolData = source.protocolData();
        result.members = GroupMemberDescription.of(source.members());
        result.state = AdminResponseMapper.mapGroupState(source.state());
        result.coordinator = Node.of(source.coordinator());
        result.isSimpleConsumerGroup = source.isSimpleConsumerGroup();
        result.authorizedOperations = AdminResponseMapper.mapAclOperations(source.authorizedOperations());
        return result;
    }
}
