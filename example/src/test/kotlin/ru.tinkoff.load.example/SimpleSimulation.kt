package ru.tinkoff.load.example

import io.gatling.javaapi.core.CoreDsl.atOnceUsers
import io.gatling.javaapi.core.CoreDsl.scenario

import ru.tinkoff.gatling.javaapi.influxdb.Annotations.Point
import ru.tinkoff.gatling.javaapi.influxdb.Annotations.userDataPoint
import ru.tinkoff.gatling.javaapi.influxdb.SimulationWithAnnotations


/** class SimulationWithAnnotations allows you to write Start annotation to influxdb before starting the simulation and Stop annotation after
 * completion of the simulation
 */
class Debug : SimulationWithAnnotations() {
    init {
        /** how to add custom actions before start of simulation
         * no need to override "before" function
         */
        before {
            println("Some action")
        }

        setUp(
            /** how to add custom points to influxdb during scenario execution
             */
            scenario("Kotlin InfluxDB")
                .exec(userDataPoint(Point("gatling").addField("status", "check")))
                .exec(
                    userDataPoint(
                        "status",
                        "check",
                        "testField",
                        "fieldValue"
                    )
                ).injectOpen(atOnceUsers(1))
                /** how to add custom points to influxdb after scenario execution
                 */
                .andThen(
                    userDataPoint("myUniqueScenario", Point("gatling", 1L))
                )
                .andThen(
                    userDataPoint(
                        "myUniqueScenario2",
                        "status",
                        "check2",
                        "testField",
                        "fieldValue2"
                    )
                )
        )

        /** how to add custom actions after end of simulation
         * No need to override "after" function
         */
        after {
            println("Some action")
        }
    }
}

