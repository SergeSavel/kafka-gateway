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

package pro.savel.kafka.admin;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.GroupState;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.acl.*;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourcePatternFilter;
import org.apache.kafka.common.resource.ResourceType;
import pro.savel.kafka.admin.data.AdminAclBinding;
import pro.savel.kafka.admin.data.AdminAclBindingFilter;
import pro.savel.kafka.admin.requests.group.AdminAlterConsumerGroupOffsetsRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminRequestMapper
{
    public static Collection<AclBindingFilter> mapAclBindingFilters(Collection<AdminAclBindingFilter> source)
    {
        if (source == null)
            return null;
        var result = new ArrayList<AclBindingFilter>(source.size());
        source.forEach(filter -> result.add(mapAclBindingFilter(filter)));
        return result;
    }

    public static AclBindingFilter mapAclBindingFilter(AdminAclBindingFilter source)
    {
        if (source == null)
            return AclBindingFilter.ANY;
        var patternFilter = mapResourcePatternFilter(source.getPatternFilter());
        var entryFilter = mapAccessControlEntryFilter(source.getEntryFilter());
        return new AclBindingFilter(patternFilter, entryFilter);
    }

    private static ResourcePatternFilter mapResourcePatternFilter(AdminAclBindingFilter.ResourcePatternFilter source)
    {
        if (source == null)
            return ResourcePatternFilter.ANY;
        var resourceType = ResourceType.valueOf(source.getResourceType());
        var name = source.getName();
        var patternType = PatternType.valueOf(source.getPatternType());
        return new ResourcePatternFilter(resourceType, name, patternType);
    }

    private static AccessControlEntryFilter mapAccessControlEntryFilter(AdminAclBindingFilter.AccessControlEntryFilter source)
    {
        if (source == null)
            return AccessControlEntryFilter.ANY;
        var principal = source.getPrincipal();
        var host = source.getHost();
        var operation = AclOperation.valueOf(source.getOperation());
        var permissionType = AclPermissionType.valueOf(source.getPermissionType());
        return new AccessControlEntryFilter(principal, host, operation, permissionType);
    }

    public static Collection<AclBinding> mapAclBindings(Collection<AdminAclBinding> source)
    {
        if (source == null)
            return null;
        var result = new ArrayList<AclBinding>(source.size());
        source.forEach(aclBindingSource -> result.add(mapAclBinding(aclBindingSource)));
        return result;
    }

    private static AclBinding mapAclBinding(AdminAclBinding source)
    {
        if (source == null)
            return null;
        var pattern = mapResourcePattern(source.getPattern());
        var entry = mapAccessControlEntry(source.getEntry());
        return new AclBinding(pattern, entry);
    }

    private static ResourcePattern mapResourcePattern(AdminAclBinding.ResourcePattern source)
    {
        if (source == null)
            return null;
        var resourceType = ResourceType.valueOf(source.getResourceType());
        var name = source.getName();
        var patternType = PatternType.valueOf(source.getPatternType());
        return new ResourcePattern(resourceType, name, patternType);
    }

    private static AccessControlEntry mapAccessControlEntry(AdminAclBinding.AccessControlEntry source)
    {
        if (source == null)
            return null;
        var principal = source.getPrincipal();
        var host = source.getHost();
        var operation = AclOperation.valueOf(source.getOperation());
        var permissionType = AclPermissionType.valueOf(source.getPermissionType());
        return new AccessControlEntry(principal, host, operation, permissionType);
    }

    /**
     * Accepts, case-insensitively, both Kafka's own state names ({@code PreparingRebalance}) and the
     * enum names the list-groups response carries ({@code PREPARING_REBALANCE}), so that a state read
     * from a response can be passed back as a filter.
     */
    public static GroupState mapGroupState(String source)
    {
        if (source == null)
            return null;
        var state = GroupState.parse(source);
        if (state != GroupState.UNKNOWN)
            return state;
        try {
            return GroupState.valueOf(source.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return GroupState.UNKNOWN;
        }
    }

    public static Map<TopicPartition, OffsetAndMetadata> mapTopicPartitionOffsetMetadata(Collection<AdminAlterConsumerGroupOffsetsRequest.TopicPartitionOffsetMetadata> source)
    {
        if (source == null)
            return null;
        var result = new HashMap<TopicPartition, OffsetAndMetadata>(source.size());
        source.forEach(item -> {
            var topicPartition = new TopicPartition(item.getTopic(), item.getPartition());
            var offsetAndMetadata = new OffsetAndMetadata(item.getOffset(), item.getMetadata());
            result.put(topicPartition, offsetAndMetadata);
        });
        return result;
    }
}
