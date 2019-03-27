package com.urms.role.ql;

public class RoleQL {
	public static final class HQL {
		public static final String RelationUser = "from User as u inner join u.roles as r where r.id = ?";
	}
}
