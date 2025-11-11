package com.nodosperifericos.domain;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * CUID generator to match Prisma's cuid() default.
 * This is a simplified version - Prisma's cuid is more complex,
 * but this will generate unique IDs that are compatible.
 */
public class CuidGenerator implements IdentifierGenerator {
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        // Generate a CUID-like string: c + timestamp + random
        // Simplified version: using UUID for now, can be enhanced to match Prisma's exact format
        return "c" + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
}

