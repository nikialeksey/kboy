package com.alexeycode.kboy.host.io

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

class GroupController(
    private val controllers: List<Controller>
) : Controller {

    override fun right(): Flow<Boolean> {
        return controllers.map { it.right() }.merge()
    }

    override fun left(): Flow<Boolean> {
        return controllers.map { it.left() }.merge()
    }

    override fun up(): Flow<Boolean> {
        return controllers.map { it.up() }.merge()
    }

    override fun down(): Flow<Boolean> {
        return controllers.map { it.down() }.merge()
    }

    override fun a(): Flow<Boolean> {
        return controllers.map { it.a() }.merge()
    }

    override fun b(): Flow<Boolean> {
        return controllers.map { it.b() }.merge()
    }

    override fun select(): Flow<Boolean> {
        return controllers.map { it.select() }.merge()
    }

    override fun start(): Flow<Boolean> {
        return controllers.map { it.start() }.merge()
    }
}