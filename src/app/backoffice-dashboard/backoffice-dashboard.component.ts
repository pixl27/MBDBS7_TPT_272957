import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { BackOfficeService } from '../shared/backoffice.service';
import { Dashboard } from './Dashboard.model';
import { GraphJourSemaine } from './GraphJourSemaine.model';
import { ChartType, ChartOptions } from 'chart.js';
import { SingleDataSet, Label, monkeyPatchChartJsLegend, monkeyPatchChartJsTooltip } from 'ng2-charts';

@Component({
  selector: 'app-backoffice-dashboard',
  templateUrl: './backoffice-dashboard.component.html',
  styleUrls: ['./backoffice-dashboard.component.css']
})
export class BackofficeDashboardComponent implements OnInit {

  dashboardtransmis!:Dashboard
  graphtransmis!:GraphJourSemaine

  public pieChartOptions: ChartOptions = {
    responsive: true,
  };
  public pieChartLabels: Label[] = ['Lundi', 'Mardi', 'Mercredi','Jeudi','Vendredi','Samedi','Dimanche'];
  public pieChartData: SingleDataSet = [];
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartPlugins = [];

  constructor(private backofficeservice:BackOfficeService,private spinner: NgxSpinnerService,private router:Router) { 
    monkeyPatchChartJsTooltip();
    monkeyPatchChartJsLegend();
  }

  ngOnInit(): void {
    this.getDashboard();
    
    this.getGraph();
  }
  getDashboard() {

    this.backofficeservice.getDashboard().subscribe((dashboard) => {
      this.dashboardtransmis = dashboard;
    });
  }
  getGraph() {

    this.backofficeservice.getGrapheJourParie().subscribe((graph) => {
      this.graphtransmis = graph;
      this.pieChartData.push(graph.lundi);
      this.pieChartData.push(graph.mardi);
      this.pieChartData.push(graph.mercredi);
      this.pieChartData.push(graph.jeudi);
      this.pieChartData.push(graph.vendredi);
      this.pieChartData.push(graph.samedi);
      this.pieChartData.push(graph.dimanche);

    });
  }
}
