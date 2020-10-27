package br.com.devcave.security.config.security

import org.springframework.security.access.prepost.PreAuthorize

@Retention(value = AnnotationRetention.RUNTIME)
@PreAuthorize("hasAnyAuthority('otherthing.delete')")
annotation class OtherthingDeletePerm