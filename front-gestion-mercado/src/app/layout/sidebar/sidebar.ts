import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { NAVIGATION_GROUPS } from '../../core/constants/navigation';
import { Session } from '../../core/services/session';
import { AppIcon } from '../../shared/components/app-icon/app-icon';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive, AppIcon],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  private readonly session = inject(Session);

  readonly navigationGroups = computed(() =>
    NAVIGATION_GROUPS.map((group) => ({
      ...group,
      links: group.links.filter((link) => this.session.hasAnyRole(link.roles)),
    })).filter((group) => group.links.length > 0),
  );
}
