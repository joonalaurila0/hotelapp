path "kv/data/users/testuser/*" {
	capabilities = ["create", "update", "read"]
}

path "kv/delete/users/testuser/*" {
	capabilities = ["delete", "update"]
}

path "kv/undelete/users/testuser/*" {
	capabilities = ["update"]
}

path "kv/destroy/users/testuser/*" {
	capabilities = ["update"]
}

path "kv/metadata/users/testuser/*" {
	capabilities = ["list", "read", "delete"]
}

path "kv/metadata/" {
	capabilities = ["list"]
}

path "kv/metadata/users/" {
	capabilities = ["list"]
}

path "kv/data/shared/*" {
	capabilities = ["read"]
}

# Write and manage secrets in key-value secrets engine
path "secret/*" {
  capabilities = ["create", "read", "update", "delete", "list"]
}

# To enable secrets engines
path "sys/mounts/*" {  
	capabilities = [ "create", "read", "update", "delete" ]
}
