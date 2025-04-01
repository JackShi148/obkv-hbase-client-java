/*-
 * #%L
 * OBKV HBase Client Framework
 * %%
 * Copyright (C) 2022 OceanBase Group
 * %%
 * OBKV HBase Client Framework  is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 * #L%
 */

package com.alipay.oceanbase.hbase.util;

import com.alipay.oceanbase.rpc.ObTableClient;
import com.alipay.oceanbase.rpc.exception.ObTableNotExistException;
import com.alipay.oceanbase.rpc.mutation.BatchOperation;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.regionserver.NoSuchColumnFamilyException;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@InterfaceAudience.Private
public class OHBaseFuncUtils {
    public static byte[][] extractFamilyFromQualifier(byte[] qualifier) throws Exception {
        int familyLen = -1;
        for (int i = 0; i < qualifier.length; i++) {
            if (qualifier[i] == '\0') {
                familyLen = i;
                break;
            }
        }
        if (familyLen == -1) {
            throw new RuntimeException("Cannot get family name");
        }
        byte[] family = Arrays.copyOfRange(qualifier, 0, familyLen);
        byte[] newQualifier = Arrays.copyOfRange(qualifier, familyLen + 1, qualifier.length);
        return new byte[][] { family, newQualifier };
    }

    public static void generateBadColumnFamilyException(BatchError batchError, final BatchOperation batch, final List<? extends Row> actions,
                                                 final ObTableNotExistException ex, final ObTableClient obTableClient,
                                                 final String realTableName, final String tableNameString) {
        String badFamily;
        if (!obTableClient.isTableGroupName(realTableName)) {
            badFamily = realTableName.split("\\$")[1];
        } else {
            String errMsg = ex.getMessage();
            int start = errMsg.indexOf('\'');
            int end = errMsg.indexOf( '\'', start + 1);
            badFamily = errMsg.substring(start + 1, end).split("\\.")[1];
        }
        String errMsg = format("Table %s:%s doesn't exist", tableNameString, badFamily);
        for (Row row : actions) {
            Set<byte[]> familySet;
            if (row instanceof Get) {
                Get get = (Get) row;
                familySet = get.familySet();
            } else {
                Mutation mutation = (Mutation) row;
                familySet = mutation.getFamilyCellMap().keySet();
            }
            if (familySet.contains(Bytes.toBytes(badFamily))) {
                batchError.add(new NoSuchColumnFamilyException(errMsg), row, null);
            }
        }
    }
}
