### Optimierung des städtischen Entwässerungsnetzentwurfs mittels Ameisenkolonieoptimierung (Optimizing urban drainage network design using ant colony optimization)

The codebase designs a drainage network by optimizing pipe diameters and selecting detention facilities to minimize costs while meeting hydraulic, regulatory, and water quality requirements.

* Hydraulic Modeling :—
  * The `DesignRainfall` class uses an intensity-duration curve to interpolate rainfall intensity for a given duration, enabling runoff calculations.
  * `CatchmentArea` calculates runoff coefficients based on land use and estimates time of concentration using an empirical formula.
  * `DrainageSolution` computes total runoff volume using rainfall intensity, catchment area, and runoff coefficients.
* Pipe Sizing :—
  * The `calculateRequiredDiameter` method in `Main` uses Manning’s equation to size pipes based on peak flow, slope, and material properties, ensuring capacity meets demand while respecting velocity constraints.
* Detention Facilities :—
  * `DetentionFacility` models detention/retention basins with capacity, release rate, and cost calculations.
  * The `evaluateDetentionRequirements` method selects a facility based on cost per volume, ensuring sufficient storage for 20% of runoff volume.
* Cost Estimation :—
  * Pipes: `DrainagePipe.calculateInstallationCost` accounts for material costs (from `PipeMaterial`) and excavation costs based on pipe diameter and depth.
  * Detention Facilities: `DetentionFacility.getCost` uses a base cost plus a capacity-dependent term, with different rates for detention vs. retention facilities.
  * Total Cost: `DrainageSolution` tracks the total cost by summing pipe and facility costs.
* Regulatory and Environmental Compliance :—
  * `RegulatoryStandards` defines constraints like maximum velocity, minimum slope, and minimum cover.
  * `WaterQualityRequirements` specifies limits for pollutants (TSS, oil/grease) and pH, with a sedimentation requirement.
  * The `calculateWaterQualityScore` method in DrainageSolution assigns scores based on HDPE pipe usage and detention facilities, though its simplistic approach (e.g., fixed increments) could be refined.
* Optimization using ant colony optimization (ACO) :—
  * Uses ants to construct solutions by selecting paths from catchment nodes to the outfall.
  * Pheromones guide path selection, updated based on solution quality (inversely proportional to cost).
  * Heuristic information combines factors like pipe length, elevation difference, drainage area, flood risk, and water quality.
  * Builds paths from catchment nodes to the outfall (node 0) using probabilistic node selection.
  * Paths are converted to pipes with calculated diameters.
  * Deposits pheromones based on solution quality and reduces pheromone levels to prevent premature convergence.
  * The implementation includes core ACO elements: pheromone trails, heuristic information, evaporation, and probabilistic path selection.
  * Parameters (`ALPHA`, `BETA`, `Q`, `EVAPORATION_RATE`) are configurable, allowing tuning of exploration vs. exploitation.
  * The heuristic combines multiple factors (distance, elevation, drainage area, water quality), making path selection context-aware.
  * Elevation factor penalizes pipes with insufficient slope, ensuring hydraulic feasibility.
  * Provides detailed metrics (cost, runoff volume, pipe utilization, water quality score), enabling comparison of solutions.
